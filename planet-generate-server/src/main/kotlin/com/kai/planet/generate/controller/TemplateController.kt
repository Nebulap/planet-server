package com.kai.planet.generate.controller

import cn.dev33.satoken.annotation.SaIgnore
import com.kai.planet.common.domain.request.generate.GenerateFileRequest
import com.kai.planet.generate.client.UserClient
import com.kai.planet.generate.service.TemplateService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @since 10/7/2024 9:40 PM
 * @author 29002
 * @version 1.0.0
 */

@SaIgnore
@RestController
@RequestMapping("/generate/temp")
class TemplateController(
    private val userClient: UserClient,
    private val templateService: TemplateService
) {
    @RequestMapping("/test2")
    fun test() = userClient.test2()


    @PostMapping("/create")
    fun generateFile(@RequestBody request: GenerateFileRequest) = templateService.generateFile(request)
}
