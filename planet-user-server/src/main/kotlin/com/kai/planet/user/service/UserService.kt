package com.kai.planet.user.service

import com.kai.planet.common.domain.dto.user.UserInfoDTO
import com.kai.planet.common.domain.dto.user.UserSignInDTO
import com.kai.planet.common.domain.entity.user.User
import com.kai.planet.common.domain.request.user.UserSignInRequest
import com.kai.planet.common.domain.request.user.UserSignUpRequest


/**
 *
 * @since 10/6/2024 11:10 PM
 * @author 29002
 * @version 1.0.0
 */


interface UserService {
    fun signIn(request: UserSignInRequest): UserSignInDTO
    fun signUp(request: UserSignUpRequest): UserSignInDTO
    fun findUser(username: String): User?
    fun findUser(userId: Long): User?
    fun getUserInfo(userId: Long): UserInfoDTO
}
