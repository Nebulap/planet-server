package com.kai.planet.gateway.controller

import com.kai.planet.common.exception.CustomException
import com.kai.planet.gateway.exception.ServerCustomExceptionCode
import jakarta.annotation.PostConstruct
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @since 10/7/2024 3:05 AM
 * @author 29002
 * @version 1.0.0
 */


@RestController
class FallbackController {

    @PostConstruct
    fun init() {
        println("FallbackController init")
    }

    @RequestMapping("/user/fallback")
    fun fallback() {
        throw CustomException(ServerCustomExceptionCode.USER_SERVICE_ERROR)
    }

    @RequestMapping("/file/fallback")
    fun fallback2() {
        throw CustomException(ServerCustomExceptionCode.FILE_SERVICE_ERROR)
    }

    @RequestMapping("/generate/fallback")
    fun fallback3() {
        throw CustomException(ServerCustomExceptionCode.GENERATE_SERVICE_ERROR)
    }
}
