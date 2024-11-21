package com.kai.planet.user.controller

import cn.dev33.satoken.annotation.SaIgnore
import com.kai.planet.common.domain.request.user.SendCodeRequest
import com.kai.planet.common.domain.request.user.UserSignInRequest
import com.kai.planet.common.domain.request.user.UserSignUpRequest
import com.kai.planet.common.domain.request.user.ValidateCodeRequest
import com.kai.planet.common.domain.response.R
import com.kai.planet.user.service.UserService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
    @PostMapping("/auth/sign-in")
    fun signIn(@Validated @RequestBody request: UserSignInRequest) = userService.signIn(request)

    @SaIgnore
    @PostMapping("/auth/sign-up")
    fun signUp(@Validated @RequestBody request: UserSignUpRequest) = userService.signUp(request)

    @SaIgnore
    @PostMapping("/auth/send-code")
    fun sendCode(@Validated @RequestBody request: SendCodeRequest): R<Void> {
        userService.sendCode(request)
        return R.ok();
    }

    @SaIgnore
    @PostMapping("/auth/validate-code")
    fun validateCode(@Validated @RequestBody request: ValidateCodeRequest): R<String> {
        return R.ok(userService.validateCode(request))
    }
}
