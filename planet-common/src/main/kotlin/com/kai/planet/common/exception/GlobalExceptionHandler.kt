package com.kai.planet.common.exception

import com.kai.planet.common.constants.http.CustomHttpHeaders
import com.kai.planet.common.domain.response.R
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 *
 * @since 10/7/2024 1:45 AM
 * @author 29002
 * @version 1.0.0
 */

@RestControllerAdvice
class GlobalExceptionHandler {

    @Autowired
    lateinit var response : HttpServletResponse
    @Autowired
    lateinit var request : HttpServletRequest

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException) :R<Void> {
        val isInternalRequest = request.getHeader(CustomHttpHeaders.X_INTERNAL_REQUEST) == true.toString()
        if (isInternalRequest){
            val originalMessage = e.exceptionCode.msg
            val encodedMessage = URLEncoder.encode(originalMessage, StandardCharsets.UTF_8.toString())
            response.addHeader(CustomHttpHeaders.X_EXCEPTION_MESSAGE,encodedMessage )
            response.addIntHeader(CustomHttpHeaders.X_EXCEPTION_CODE, e.exceptionCode.code)
        }

        return  R.fail(e.exceptionCode.msg, e.exceptionCode.code)
    }

    @ExceptionHandler(CustomMessageException::class)
    fun handleCustomMessageException(e: CustomMessageException) :R<Void> {
        return  R.fail(e.message, e.code)
    }
}
