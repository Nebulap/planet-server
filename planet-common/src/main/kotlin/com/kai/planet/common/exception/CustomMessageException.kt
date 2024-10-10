package com.kai.planet.common.exception

/**
 *
 * @since 10/10/2024 9:27 PM
 * @author 29002
 * @version 1.0.0
 */


class CustomMessageException(
    override val message: String,
    val code: Int
) : RuntimeException()
