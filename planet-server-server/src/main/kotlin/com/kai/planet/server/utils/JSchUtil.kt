package com.kai.planet.server.utils


import com.jcraft.jsch.*
import com.kai.planet.common.domain.entity.server.Remote
import com.kai.planet.common.exception.CustomException
import com.kai.planet.common.exception.GlobalExceptionCode
import com.kai.planet.server.exception.ServerCustomExceptionCode
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

    const val SESSION_TIMEOUT = 5000
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


    fun scpTo(session: Session, source: String, destination: String) {
        println("scpTo 1") // 1
        var fileInputStream: FileInputStream? = null
        println("scpTo 2") // 2
        var channel: ChannelExec? = null
        println("scpTo 3") // 3
        try {
            channel = openExecChannel(session)
            println("scpTo 4") // 4

            val out = channel.outputStream
            println("scpTo 5") // 5
            val `in` = channel.inputStream
            println("scpTo 6") // 6
            var command = "scp -t $destination"
            println("scpTo 7") // 7
            channel.setCommand(command)
            println("scpTo 8") // 8
            channel.connect(CONNECT_TIMEOUT)
            println("scpTo 9") // 9

            if (checkAck(`in`) != 0) return
            println("scpTo 10") // 10
            val file = File(source)
            println("scpTo 11") // 11
            val fileSize = file.length()
            println("scpTo 12") // 12
            command = "C0644 $fileSize ${file.name}\n"
            println("scpTo 13") // 13
            out.write(command.toByteArray())
            println("scpTo 14") // 14
            out.flush()
            println("scpTo 15") // 15

            if (checkAck(`in`) != 0) return
            println("scpTo 16") // 16

            fileInputStream = FileInputStream(source)
            println("scpTo 17") // 17

            val buf = ByteArray(1024)
            println("scpTo 18") // 18
            var sum: Long = 0
            println("scpTo 19") // 19
            while (true) {
                val len = fileInputStream.read(buf)
                println("scpTo 20") // 20
                if (len <= 0) break
                println("scpTo 21") // 21
                out.write(buf, 0, len)
                println("scpTo 22") // 22
                sum += len
                println("scpTo 23") // 23
            }
            buf[0] = 0
            println("scpTo 24") // 24
            out.write(buf, 0, 1)
            println("scpTo 25") // 25
            out.flush()
            println("scpTo 26") // 26

            if (checkAck(`in`) != 0) return
            println("scpTo 27") // 27
        } catch (e: Exception) {
            println("scpTo 28") // 28
            throw CustomException(ServerCustomExceptionCode.SESSION_NULL)
        } finally {
            println("scpTo 29") // 29
            closeInputStream(fileInputStream)
            println("scpTo 30") // 30
            disconnect(channel)
            println("scpTo 31") // 31
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
            disconnect(channel)
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
    fun disconnect(session: Session?) {
        if (session == null) return
        if (session.isConnected) {
            session.disconnect()
        }
    }

    fun disconnect(channel: Channel?) {
        if (channel == null) return
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
        if (input == null) return
        input.close()
    }

    fun closeOutputStream(output: OutputStream?) {
        if (output == null) return
        output.close()
    }
}
