package com.kai.planet.mail.service

import com.kai.planet.common.domain.request.email.SendSimpleMailRequest

/**
 *
 * @since 2024/11/18 20:39
 * @author 29002
 * @version 1.0.0
 */


interface EmailService {
    fun sendSimpleMail(request: SendSimpleMailRequest)
}
