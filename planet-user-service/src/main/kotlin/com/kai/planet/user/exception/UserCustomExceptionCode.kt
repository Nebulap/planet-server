package com.kai.planet.user.exception

import com.kai.planet.common.exception.CustomExceptionCode

/**
 *
 * @since 10/7/2024 1:25 AM
 * @author 29002
 * @version 1.0.0
 */


enum class UserCustomExceptionCode(override val code: Int, override val msg: String) : CustomExceptionCode {
    USER_NOT_FOUND(10000, "用户名不存在"),
    USER_NOT_FOUND_OR_PASSWORD_ERROR(10001, "用户名不存在或密码错误"),
    PERMISSION_DENIED(10002, "权限不足"),
    USER_ALREADY_EXISTS(10003, "用户名已存在"),
    USER_ROLE_EXISTS(10004, "用户角色已存在"),
    USER_ROLE_NOT_EXISTS(10005, "用户角色不存在")
}
