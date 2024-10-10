package com.kai.planet.user.controller

import cn.dev33.satoken.annotation.SaIgnore
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

    @SaIgnore
    @PostMapping("/sign-in")
    fun signIn(@Validated @RequestBody request: UserSignInRequest) = userService.signIn(request)

    @SaIgnore
    @PostMapping("/sign-up")
    fun signUp(@Validated @RequestBody request: UserSignUpRequest) = userService.signUp(request)

    @SaIgnore
    @GetMapping("/test")
    fun test(): String {
        throw CustomException(UserCustomExceptionCode.USER_ROLE_EXISTS)
    }
    @SaIgnore
    @GetMapping("/test1")
    fun test1(): String {
        val a = 1/0;
        return ""
    }
}
