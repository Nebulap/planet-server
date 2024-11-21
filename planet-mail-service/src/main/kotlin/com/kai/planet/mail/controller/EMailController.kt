package com.kai.planet.mail.controller

import com.kai.planet.common.domain.request.email.SendSimpleMailRequest
import com.kai.planet.common.domain.response.R
import com.kai.planet.mail.service.EmailService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @since 2024/11/18 20:40
 * @author 29002
 * @version 1.0.0
 */


@RestController
@RequestMapping("/email")
class EmailController(
    private val emailService: EmailService
) {
    @PostMapping("/sendSimpleMail")
    fun sendSimpleMail(@RequestBody request: SendSimpleMailRequest): R<Void> {
        emailService.sendSimpleMail(request)
        return R.ok()
    }
}
