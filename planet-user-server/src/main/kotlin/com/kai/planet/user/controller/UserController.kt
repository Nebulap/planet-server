package com.kai.planet.user.controller

import com.kai.planet.common.annotation.Roles
import com.kai.planet.common.constants.user.RoleEnum
import com.kai.planet.common.domain.request.user.UserSignInRequest
import com.kai.planet.common.domain.request.user.UserSignUpRequest
import com.kai.planet.common.exception.CustomException
import com.kai.planet.user.exception.UserCustomExceptionCode
import com.kai.planet.user.service.UserService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 *
 * @since 10/6/2024 11:26 PM
 * @author 29002
 * @version 1.0.0
 */


@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/sign-in")
    fun signIn(@Validated @RequestBody request: UserSignInRequest) = userService.signIn(request)

    @PostMapping("/sign-up")
    fun signUp(@Validated @RequestBody request: UserSignUpRequest) = userService.signUp(request)

    @Roles(RoleEnum.ADMIN)
    @GetMapping("/test")
    fun test(): String {
        throw CustomException(UserCustomExceptionCode.USER_ROLE_EXISTS)
    }

    @Roles(RoleEnum.USER)
    @GetMapping("/test1")
    fun test1(): String {
        throw CustomException(UserCustomExceptionCode.USER_ROLE_EXISTS)
    }
}
