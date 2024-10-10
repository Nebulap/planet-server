package com.kai.planet.common.domain.request.user

import jakarta.validation.constraints.NotBlank

/**
 *
 * @since 10/6/2024 11:12 PM
 * @author 29002
 * @version 1.0.0
 */


data class UserSignInRequest(
    @NotBlank(message = "用户名不能为空") val username: String,
    @NotBlank(message = "密码不能为空") val password: String
)
