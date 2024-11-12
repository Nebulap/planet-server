package com.kai.planet.server.service.impl

import com.alibaba.fastjson2.JSON
import com.kai.planet.common.constants.server.ServerState
import com.kai.planet.common.domain.dto.server.ServerDTO
import com.kai.planet.common.domain.dto.server.ServerStateDTO
import com.kai.planet.common.domain.entity.server.Remote
import com.kai.planet.common.domain.entity.server.ServerUser
import com.kai.planet.common.domain.request.server.AddServerRequest
import com.kai.planet.common.domain.request.server.GetServerListRequest
import com.kai.planet.common.domain.request.server.GetServerStateRequest
import com.kai.planet.common.exception.CustomException
import com.kai.planet.server.exception.ServerCustomExceptionCode
import com.kai.planet.server.mapper.ServerMapper
import com.kai.planet.server.service.ServerService
import com.kai.planet.server.utils.JSchUtil
import com.mybatisflex.kotlin.extensions.db.filter
import com.mybatisflex.kotlin.extensions.db.filterOne
import com.mybatisflex.kotlin.extensions.kproperty.eq
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

/**
 *
 * @since 2024/11/10 21:56
 * @author 29002
 * @version 1.0.0
 */


@Service
class ServerServiceImpl(
//    private val serverConvert: ServerConvert,
    private val serverConvert: ServerMapper,
    private val redisTemplate: RedisTemplate<String, Any>
) : ServerService {

    private final val serverStateKey = "server:state"
    private fun getServerStateListKey(userId: Long): String {
        return "$serverStateKey:$userId"
    }

    override fun getServerList(request: GetServerListRequest): List<ServerDTO> {
        val userId = 1L;

        val serverIdList: List<Long> = filter<ServerUser> { (ServerUser::userId eq userId) }.map { it.serverId }

//        return filter<Server> { (Server::id `in` serverIdList) }.map { server -> serverConvert.toDTO(server) }
        return Collections.emptyList()
    }

    override fun getServerState(request: GetServerStateRequest): ServerStateDTO {
        val remote = filterOne<Remote> { (Remote::id eq request.remoteId) } ?: throw CustomException(ServerCustomExceptionCode.REMOTE_NOT_FOUND)

        val session = JSchUtil.getSession(remote)
        //TODO should be replaced with a more secure way
        JSchUtil.scpTo(session, "C:\\Users\\29002\\Desktop\\state.sh", "/home/forest/planet/state.sh")
        JSchUtil.remoteExecute(session, "sed -i \"s/\\r//\" /home/forest/planet/state.sh")
        JSchUtil.remoteExecute(session, "chmod +x /home/forest/planet/state.sh")

        val string = StringBuilder()
        JSchUtil.asyncRemoteExecute(session,"/home/forest/planet/state.sh")
//        JSchUtil.asyncRemoteExecute(session,"pkill -f '/home/forest/planet/state.sh'")
//        val result = JSchUtil.remoteExecute(session, "/home/forest/planet/state.sh")
//        result.forEach {
//            string.append(it)
//        }
//        println(string)
//        // Close the session, if not closed, the request will be blocked
//        session.disconnect()
//        return try {
//            val state =  JSON.parseObject(string.toString(), ServerStateDTO::class.java)
//            state.status = ServerState.RUNNING
//            state.lastUpdated = LocalDateTime.now()
//            return state
//        } catch (e: Exception) {
//            e.printStackTrace()
//            ServerStateDTO(
//                status = ServerState.ERROR,
//                cpuUsage = "--",
//                memUsage = "--",
//                memFree = "--",
//                diskTotal = "--",
//                diskUsage = "--",
//                lastUpdated = LocalDateTime.now()
//            )
//        }
//
        return             ServerStateDTO(
            status = ServerState.ERROR,
            cpuUsage = "--",
            memUsage = "--",
            memFree = "--",
            diskTotal = "--",
            diskUsage = "--",
            lastUpdated = LocalDateTime.now()
        )

    }

    override fun addServer(request: AddServerRequest) {
        val server = serverConvert.toEntity(request)
        println(JSON.toJSONString(server))
    }
}

