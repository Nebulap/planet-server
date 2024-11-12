package com.kai.planet.server.controller

import cn.dev33.satoken.annotation.SaIgnore
import com.kai.planet.common.domain.dto.server.ServerDTO
import com.kai.planet.common.domain.dto.server.ServerStateDTO
import com.kai.planet.common.domain.request.server.GetServerListRequest
import com.kai.planet.common.domain.request.server.GetServerStateRequest
import com.kai.planet.server.service.ServerService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 *
 * @since 2024/11/10 18:49
 * @author 29002
 * @version 1.0.0
 */


@RestController
@RequestMapping("/server")
class ServerController(
    private val serverService: ServerService
) {


    @SaIgnore
    @PostMapping("/get-server-list")
    fun getServerList(@RequestBody request: GetServerListRequest): List<ServerDTO> {
        return serverService.getServerList(request)
    }

    @SaIgnore
    @PostMapping("/get-server-state")
    fun getServerState(@RequestBody request: GetServerStateRequest): ServerStateDTO {
        return serverService.getServerState(request)
    }

    @SaIgnore
    @PostMapping("/test")
    fun test(@RequestBody request: Any): Any {
        return request;
    }
}
