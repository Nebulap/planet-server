package com.kai.planet.gateway.exception

import com.kai.planet.common.domain.response.R
import com.kai.planet.common.exception.CustomException
import com.kai.planet.common.exception.CustomMessageException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 *
 * @since 10/7/2024 1:45 AM
 * @author 29002
 * @version 1.0.0
 */

@RestControllerAdvice
class GatewayExceptionHandler {

    @ResponseBody
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException) :R<Void> {
        return  R.fail(e.exceptionCode.msg, e.exceptionCode.code)
    }

    @ResponseBody
    @ExceptionHandler(CustomMessageException::class)
    fun handleCustomMessageException(e: CustomMessageException) :R<Void> {
        return  R.fail(e.message, e.code)
    }

}
