package com.kai.planet.generate.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

/**
 *
 * @since 10/9/2024 6:28 PM
 * @author 29002
 * @version 1.0.0
 */


@FeignClient(name = "userService", url = "http://localhost:8081")
interface UserClient {

    @GetMapping("/user/test2")
    fun test2(): String
}
