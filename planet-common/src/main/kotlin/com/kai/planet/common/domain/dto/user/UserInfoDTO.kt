package com.kai.planet.common.domain.dto.user

import com.kai.planet.common.domain.entity.user.Role

/**
 *
 * @since 10/9/2024 3:05 PM
 * @author 29002
 * @version 1.0.0
 */


data class UserInfoDTO(
    val id: Long,
    val email: String,
    val avatar: String,
    val nickname: String,
    val username: String? = null,
    val roles: List<Role>? = null,
    val currentRole: Role? = null
)
