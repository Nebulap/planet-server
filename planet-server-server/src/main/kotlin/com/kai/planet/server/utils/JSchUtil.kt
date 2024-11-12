package com.kai.planet.server.utils


import com.jcraft.jsch.*
import com.kai.planet.common.domain.entity.server.Remote
import com.kai.planet.common.exception.CustomException
import com.kai.planet.common.exception.GlobalExceptionCode
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths


/**
 *
 * @since 2024/11/10 21:47
 * @author 29002
 * @version 1.0.0
 */
object JSchUtil {

    const val SESSION_TIMEOUT = 30000
    const val CONNECT_TIMEOUT = 3000

    /**
     * 获取会话
     *
     * @param remote 远程服务器信息
     * @return 会话
     * @throws JSchException
     */
    @Throws(JSchException::class)
    fun getSession(remote: Remote): Session {
        val jSch = JSch()
        if (Files.exists(Paths.get(remote.identity))) {
            jSch.addIdentity(remote.identity, remote.passphrase)
        }
        val session = jSch.getSession(remote.user, remote.host, remote.port)
        session.setPort(22)
        session.setPassword(remote.password)
        session.setConfig("StrictHostKeyChecking", "no")
        return session
    }

    /**
     * 远程执行命令
     *
     * @param session 会话
     * @param command 命令
     * @return 结果
     * @throws JSchException
     */
    @Throws(JSchException::class)
    fun remoteExecute(session: Session, command: String): List<String> {
        val resultLines = mutableListOf<String>()
        var channel: ChannelExec? = null
        try {
            channel = openExecChannel(session)
            channel.setCommand(command)
            val input = channel.inputStream
            channel.connect(CONNECT_TIMEOUT)
            input.bufferedReader().use { reader ->
                reader.forEachLine { resultLines.add(it) }
            }
        } catch (e: IOException) {
            throw CustomException(GlobalExceptionCode.IO_ERROR)
        } finally {
            disconnect(channel!!)
        }
        return resultLines
    }

    /**
     *异步远程执行命令
     */
    @Throws(JSchException::class)
    fun asyncRemoteExecute(session: Session, command: String) {
        var channel: ChannelExec? = null
        try {
            channel = openExecChannel(session)
            channel.setCommand(command)
            channel.connect(CONNECT_TIMEOUT)
        } catch (e: IOException) {
            throw CustomException(GlobalExceptionCode.IO_ERROR)
        } finally {
            disconnect(channel!!)
        }
    }

    /**
     * scp复制文件到远程指定路径
     *
     * @param session 会话
     * @param source 文件源
     * @param destination 远程目标路径
     * @throws IOException
     * @throws JSchException
     */
    @Throws(IOException::class, JSchException::class)
    fun scpTo(session: Session, source: String, destination: String) {
        var fileInputStream: FileInputStream? = null
        var channel: ChannelExec? = null
        try {
            channel = openExecChannel(session)
            val out = channel.outputStream
            val `in` = channel.inputStream
            var command = "scp -t $destination"
            channel.setCommand(command)
            channel.connect(CONNECT_TIMEOUT)

            if (checkAck(`in`) != 0) return
            val file = File(source)
            val fileSize = file.length()
            command = "C0644 $fileSize ${file.name}\n"
            out.write(command.toByteArray())
            out.flush()

            if (checkAck(`in`) != 0) return

            fileInputStream = FileInputStream(source)

            val buf = ByteArray(1024)
            var sum: Long = 0
            while (true) {
                val len = fileInputStream.read(buf)
                if (len <= 0) break
                out.write(buf, 0, len)
                sum += len
            }
            buf[0] = 0
            out.write(buf, 0, 1)
            out.flush()

            if (checkAck(`in`) != 0) return
        } catch (e: Exception) {
            throw e
        } finally {
            closeInputStream(fileInputStream)
            disconnect(channel!!)
        }
    }

    /**
     * scp获取远程文件到指定目录
     *
     * @param session 会话
     * @param source 远程源路径
     * @param destination 本地目标路径
     * @throws IOException
     * @throws JSchException
     */
    @Throws(IOException::class, JSchException::class)
    fun scpFrom(session: Session, source: String, destination: String) {
        var fileOutputStream: FileOutputStream? = null
        var channel: ChannelExec? = null
        try {
            channel = openExecChannel(session)
            channel.setCommand("scp -f $source")
            val out = channel.outputStream
            val `in` = channel.inputStream
            channel.connect()

            val buf = ByteArray(1024)
            buf[0] = 0
            out.write(buf, 0, 1)
            out.flush()

            while (true) {
                if (checkAck(`in`) != 'C'.toInt()) {
                    break
                }
            }
            `in`.read(buf, 0, 4)
            var fileSize: Long = 0
            while (true) {
                if (`in`.read(buf, 0, 1) < 0) break
                if (buf[0] == ' '.toByte()) break
                fileSize = fileSize * 10L + (buf[0] - '0'.toByte()).toLong()
            }

            var file: String? = null
            for (i in buf.indices) {
                `in`.read(buf, i, 1)
                if (buf[i] == '\n'.toByte()) {
                    file = String(buf, 0, i)
                    break
                }
            }

            buf[0] = 0
            out.write(buf, 0, 1)
            out.flush()

            fileOutputStream = if (Files.isDirectory(Paths.get(destination))) {
                FileOutputStream("$destination${File.separator}$file")
            } else {
                FileOutputStream(destination)
            }

            var sum: Long = 0
            while (true) {
                val len = `in`.read(buf)
                if (len <= 0) break
                sum += len
                if (len >= fileSize) {
                    fileOutputStream.write(buf, 0, fileSize.toInt())
                    break
                }
                fileOutputStream.write(buf, 0, len)
                fileSize -= len
            }
        } finally {
            closeOutputStream(fileOutputStream)
            disconnect(channel!!)
        }
    }

    /**
     * 打开 channel
     */
    @Throws(JSchException::class)
    fun openChannel(session: Session, type: String): Channel {
        if (!session.isConnected) {
            session.connect(SESSION_TIMEOUT)
        }
        return session.openChannel(type)
    }

    /**
     * 打开 SFTP channel
     */
    @Throws(JSchException::class)
    fun openSftpChannel(session: Session): ChannelSftp {
        return openChannel(session, "sftp") as ChannelSftp
    }

    /**
     * 打开 Exec channel
     */
    @Throws(JSchException::class)
    fun openExecChannel(session: Session): ChannelExec {
        return openChannel(session, "exec") as ChannelExec
    }

    /**
     * 关闭连接
     */
    fun disconnect(session: Session) {
        if (session.isConnected) {
            session.disconnect()
        }
    }

    fun disconnect(channel: Channel) {
        if (channel.isConnected) {
            channel.disconnect()
        }
    }

    fun checkAck(input: InputStream): Int {
        val b = input.read()
        if (b == 0) {
            return b
        }
        if (b == -1) {
            return b
        }
        if (b == 1 || b == 2) {
            val sb = StringBuilder()
            var c: Int
            do {
                c = input.read()
                sb.append(c.toChar())
            } while (c != '\n'.toInt())
        }
        return b
    }

    fun closeInputStream(input: InputStream?) {
        input?.close()
    }

    fun closeOutputStream(output: OutputStream?) {
        output?.close()
    }
}
