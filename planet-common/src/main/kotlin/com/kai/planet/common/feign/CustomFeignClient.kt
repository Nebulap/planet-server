package com.kai.planet.common.feign

import com.kai.planet.common.constants.http.CustomHttpHeaders
import com.kai.planet.common.exception.CustomMessageException
import feign.Client
import feign.Request
import feign.Response
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 *
 * @since 10/10/2024 8:48 PM
 * @author 29002
 * @version 1.0.0
 */


open class CustomFeignClient(
    private val delegate: Client = Client.Default(null, null)
) : Client {

    override fun execute(p0: Request?, p1: Request.Options?): Response {

        val response = delegate.execute(p0, p1)
        val headers = response.headers()
        val exceptionCode = headers[CustomHttpHeaders.X_EXCEPTION_CODE]?.firstOrNull()

        // If the exception code is not null, throw a custom exception with the message and code
        if (exceptionCode != null) {
            val encodeExceptionMessage = headers[CustomHttpHeaders.X_EXCEPTION_MESSAGE]!!.first()
            val message = URLDecoder.decode(encodeExceptionMessage, StandardCharsets.UTF_8)
            throw CustomMessageException(message, exceptionCode.toInt())
        }

        return response
    }
}
