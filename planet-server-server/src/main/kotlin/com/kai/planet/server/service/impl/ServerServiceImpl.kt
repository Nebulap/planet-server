package com.kai.planet.server.service.impl

import com.alibaba.fastjson2.JSON
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
import com.mybatisflex.kotlin.extensions.kproperty.eq
import com.mybatisflex.kotlin.extensions.kproperty.`in`
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
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
        println(JSON.toJSONString(state))
        return state
    }

    override fun addServer(request: AddServerRequest) {
        val dbServer = filterOne<Server> { (Server::ip eq request.ip) }
        if (dbServer != null) {
            throw CustomException(ServerCustomExceptionCode.SERVER_FOUND)
        }
        val server = serverMapper.toEntity(request)
        insert(server)
        val remote = request.remote
        remote.serverId = server.id!!
        insert(remote)

        val userId = 1L
        val root = HashMap<String, Any>();
        root["url"] = "http://192.168.160.1:8080/server/set-server-state?id=$userId&host=${server.ip}"
        val fileResponseDTO = generateClient.generateFile(GenerateFileRequest(1, root))
        val file = fileClient.getFileById(fileResponseDTO.uuid)!!

        val session = JSchUtil.getSession(remote)
        //TODO should be replaced with a more secure way, now root user is not allowed
        JSchUtil.scpTo(session, file.path, "/home/${remote.user}/planet/state.sh")
        JSchUtil.remoteExecute(session, "sed -i \"s/\\r//\" /home/${remote.user}/planet/state.sh")
        JSchUtil.remoteExecute(session, "chmod +x /home/${remote.user}/planet/state.sh")

        JSchUtil.asyncRemoteExecute(session, "pkill -f '/home/${remote.user}/planet/state.sh'")
        JSchUtil.asyncRemoteExecute(session, "/home/${remote.user}/planet/state.sh")
        session.disconnect()
    }

    /**
     *  receive server state from remote server and save to redis
     */
    override fun setServerState(request: ServerStateDTO, id: Long, host: String) {
        val key = getServerStateListKey(id)
        redisTemplate.opsForHash<String, ServerStateDTO>().put(key, host, request)
    }
}

