package com.kai.planet.common.exception

/**
 *
 * @since 10/7/2024 1:28 AM
 * @author 29002
 * @version 1.0.0
 */


class CustomException(
    val exceptionCode: CustomExceptionCode
) : RuntimeException()
