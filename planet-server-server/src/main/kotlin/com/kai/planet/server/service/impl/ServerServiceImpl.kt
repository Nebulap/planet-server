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

    @Transactional(rollbackFor = [Exception::class])
    override fun addServer(request: AddServerRequest) {
        val dbServer = filterOne<Server> { (Server::ip eq request.ip) }
        if (dbServer != null) {
            throw CustomException(ServerCustomExceptionCode.SERVER_FOUND)
        }
        val server = serverMapper.toEntity(request)
        insert(server)

        val userServer = ServerUser(userId = 1L, serverId = server.id!!)
        insert(userServer)

        val remote = request.remote
        remote.serverId = server.id!!
        insert(remote)

        val userId = 1L
        val root = HashMap<String, Any>();
        root["url"] = "http://192.168.160.1:8080/server/set-server-state?id=$userId&host=${server.ip}"
        val stateFileResponseDTO = generateClient.generateFile(GenerateFileRequest(1, root))
        val stateFile = fileClient.getFileById(stateFileResponseDTO.uuid)!!

        val session = JSchUtil.getSession(remote)
        //TODO should be replaced with a more secure way, now root user is not allowed
        transferAndExecuteScriptAsync(session, stateFile.path, remote.user, "/home/${remote.user}/planet/state.sh")

        if (request.autoFullInfo) {
            val serverInfoFileResponseDTO = generateClient.generateFile(GenerateFileRequest(2, root))
            val serverInfoFile = fileClient.getFileById(serverInfoFileResponseDTO.uuid)!!
            val result = transferAndExecuteScript(session, serverInfoFile.path, remote.user, "/home/${remote.user}/planet/server-info.sh")
            val info = JSON.parseObject(result[0], Server::class.java)
            update<Server> {
                Server::os set info.os
                Server::memory set info.memory
                Server::disk set info.disk
                Server::cpuCores set info.cpuCores
                whereWith { Server::id eq server.id }
            }
        }

        session.disconnect()
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

