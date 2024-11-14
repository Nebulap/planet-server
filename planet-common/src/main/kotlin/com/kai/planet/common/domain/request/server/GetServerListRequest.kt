package com.kai.planet.common.domain.request.server

/**
 *
 * @since 2024/11/10 21:51
 * @author 29002
 * @version 1.0.0
 */


data class GetServerListRequest(
    val userId: Long,
    val state: String? = null,
    val tags: List<String>? = null,
    val host: String? = null
)
