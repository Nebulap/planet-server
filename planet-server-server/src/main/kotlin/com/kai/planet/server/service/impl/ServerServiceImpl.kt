package com.kai.planet.server.service.impl

import com.alibaba.fastjson2.JSON
import com.jcraft.jsch.Session
import com.kai.planet.common.constants.server.ServerState
import com.kai.planet.common.domain.dto.server.ServerDTO
import com.kai.planet.common.domain.dto.server.ServerStateDTO
import com.kai.planet.common.domain.entity.server.Server
import com.kai.planet.common.domain.entity.server.ServerUser
import com.kai.planet.common.domain.request.generate.GenerateFileRequest
import com.kai.planet.common.domain.request.server.AddServerRequest
import com.kai.planet.common.domain.request.server.GetServerListRequest
import com.kai.planet.common.domain.request.server.GetServerStateRequest
import com.kai.planet.common.exception.CustomException
import com.kai.planet.server.client.FileClient
import com.kai.planet.server.client.GenerateClient
import com.kai.planet.server.exception.ServerCustomExceptionCode
import com.kai.planet.server.mapper.ServerMapper
import com.kai.planet.server.service.ServerService
import com.kai.planet.server.utils.JSchUtil
import com.mybatisflex.kotlin.extensions.condition.and
import com.mybatisflex.kotlin.extensions.db.filter
import com.mybatisflex.kotlin.extensions.db.filterOne
import com.mybatisflex.kotlin.extensions.db.insert
import com.mybatisflex.kotlin.extensions.db.update
import com.mybatisflex.kotlin.extensions.kproperty.eq
import com.mybatisflex.kotlin.extensions.kproperty.`in`
import com.mybatisflex.kotlin.extensions.wrapper.whereWith
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 *
 * @since 2024/11/10 21:56
 * @author 29002
 * @version 1.0.0
 */


@Service
class ServerServiceImpl(
    private val generateClient: GenerateClient,
    private val fileClient: FileClient,
    private val serverMapper: ServerMapper,
    private val redisTemplate: RedisTemplate<String, Any>
) : ServerService {

    private final val serverStateKey = "server:state"
    private fun getServerStateListKey(userId: Long): String {
        return "$serverStateKey:$userId"
    }

    override fun getServerList(request: GetServerListRequest): List<ServerDTO> {
        val userId = 1L;

        val serverIdList: List<Int> = filter<ServerUser> { (ServerUser::userId eq userId) }.map { it.serverId }

        return filter<Server> {
            (Server::id `in` serverIdList)
                .and(request.host != null) { Server::ip eq request.host }
        }.map { server -> serverMapper.toDTO(server) }
    }

    /**
     * get server state from redis, consider optimizing it to WebSocket
     */
    override fun getServerState(request: GetServerStateRequest): ServerStateDTO {
        val key = getServerStateListKey(1L)
        val state = redisTemplate.opsForHash<String, ServerStateDTO>().get(key, request.host) ?: return ServerStateDTO()
        val time = ChronoUnit.SECONDS.between(state.lastUpdated, LocalDateTime.now())
        state.status = if (time > 10) ServerState.ERROR else ServerState.RUNNING
        return state
    }


    @Async
    @Transactional(rollbackFor = [Exception::class])
    override fun addServer(request: AddServerRequest) {
        val dbServer = filterOne<Server> { (Server::ip eq request.ip) }
        println("Line 1") // 1
        if (dbServer != null) {
            println("Line 2") // 2
            throw CustomException(ServerCustomExceptionCode.SERVER_FOUND)
        }
        println("Line 3") // 3
        val server = serverMapper.toEntity(request)
        println("Line 4") // 4
        insert(server)
        println("Line 5") // 5

        val userServer = ServerUser(userId = 1L, serverId = server.id!!)
        println("Line 6") // 6
        insert(userServer)
        println("Line 7") // 7

        val remote = request.remote
        println("Line 8") // 8
        remote.serverId = server.id!!
        println("Line 9") // 9
        remote.host = server.ip!!
        println("Line 10") // 10
        insert(remote)
        println("Line 11") // 11

        val userId = 1L
        println("Line 12") // 12
        val root = HashMap<String, Any>()
        println("Line 13") // 13
        root["url"] = "http://192.168.160.1:8080/server/set-server-state?id=$userId&host=${server.ip}"
        println("Line 14") // 14
        val stateFileResponseDTO = generateClient.generateFile(GenerateFileRequest(1, root))
        println("Line 15") // 15
        val stateFile = fileClient.getFileById(stateFileResponseDTO.uuid)!!
        println("Line 16") // 16

        val session = JSchUtil.getSession(remote)
        println("Line 17") // 17
        //TODO should be replaced with a more secure way, now root user is not allowed
        transferAndExecuteScriptAsync(session, stateFile.path, remote.user, "/home/${remote.user}/planet/state.sh")
        println("Line 18") // 18

        if (request.autoFullInfo) {
            println("Line 19") // 19
            val serverInfoFileResponseDTO = generateClient.generateFile(GenerateFileRequest(2, root))
            println("Line 20") // 20
            val serverInfoFile = fileClient.getFileById(serverInfoFileResponseDTO.uuid)!!
            println("Line 21") // 21
            val result = transferAndExecuteScript(session, serverInfoFile.path, remote.user, "/home/${remote.user}/planet/server-info.sh")
            println("Line 22") // 22
            val info = JSON.parseObject(result[0], Server::class.java)
            println("Line 23") // 23
            update<Server> {
                Server::os set info.os
                println("Line 24") // 24
                Server::memory set info.memory
                println("Line 25") // 25
                Server::disk set info.disk
                println("Line 26") // 26
                Server::cpuCores set info.cpuCores
                println("Line 27") // 27
                whereWith { Server::id eq server.id }
            }
            println("Line 28") // 28
        }

        session.disconnect()
        println("Line 29") // 29
    }


    /**
     *  receive server state from remote server and save to redis
     */
    override fun setServerState(request: ServerStateDTO, id: Long, host: String) {
        val key = getServerStateListKey(id)
        redisTemplate.opsForHash<String, ServerStateDTO>().put(key, host, request)
    }

    fun transferAndExecuteScriptAsync(session: Session, localFilePath: String, user: String, scriptPath: String) {
        JSchUtil.scpTo(session, localFilePath, scriptPath)
        JSchUtil.remoteExecute(session, "sed -i \"s/\\r//\" $scriptPath")
        JSchUtil.remoteExecute(session, "chmod +x $scriptPath")

        JSchUtil.asyncRemoteExecute(session, "pkill -f '$scriptPath'")
        JSchUtil.asyncRemoteExecute(session, "nohup $scriptPath")
    }

    fun transferAndExecuteScript(session: Session, localFilePath: String, user: String, scriptPath: String): List<String> {
        JSchUtil.scpTo(session, localFilePath, scriptPath)
        JSchUtil.remoteExecute(session, "sed -i \"s/\\r//\" $scriptPath")
        JSchUtil.remoteExecute(session, "chmod +x $scriptPath")

        JSchUtil.asyncRemoteExecute(session, "pkill -f '$scriptPath'")
        return JSchUtil.remoteExecute(session, scriptPath)
    }
}

