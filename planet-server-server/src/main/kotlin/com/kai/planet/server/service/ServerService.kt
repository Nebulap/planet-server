package com.kai.planet.server.service

import com.kai.planet.common.domain.dto.server.ServerDTO
import com.kai.planet.common.domain.dto.server.ServerStateDTO
import com.kai.planet.common.domain.request.server.AddServerRequest
import com.kai.planet.common.domain.request.server.GetServerListRequest
import com.kai.planet.common.domain.request.server.GetServerStateRequest

/**
 *
 * @since 2024/11/10 21:14
 * @author 29002
 * @version 1.0.0
 */


interface ServerService {
    fun getServerList(request:GetServerListRequest): List<ServerDTO>
    fun getServerState(request: GetServerStateRequest):ServerStateDTO
    fun addServer(request: AddServerRequest)
}
