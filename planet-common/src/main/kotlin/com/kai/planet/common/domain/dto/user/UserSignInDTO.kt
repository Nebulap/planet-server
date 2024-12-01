package com.kai.planet.common.domain.dto.user

/**
 *
 * @since 10/7/2024 1:41 AM
 * @author 29002
 * @version 1.0.0
 */


data class UserSignInDTO(
    val token: String,
    val user:UserInfoDTO
)
