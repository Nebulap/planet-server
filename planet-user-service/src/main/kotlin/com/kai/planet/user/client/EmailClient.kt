package com.kai.planet.user.client

import com.kai.planet.common.domain.request.email.SendSimpleMailRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

/**
 *
 * @since 2024/11/18 21:08
 * @author 29002
 * @version 1.0.0
 */


@FeignClient(name="emailService", url = "http://localhost:8084/email")
interface EmailClient {
    @PostMapping("/sendSimpleMail")
    fun sendSimpleMail(request: SendSimpleMailRequest)
}
