package com.kai.planet.server.exception

import com.kai.planet.common.exception.CustomExceptionCode

/**
 *
 * @since 2024/11/10 21:16
 * @author 29002
 * @version 1.0.0
 */


enum class ServerCustomExceptionCode(override val code: Int, override val msg: String) : CustomExceptionCode {
    SERVER_NOT_FOUND(30001, "服务器不存在"),
    SERVER_NOT_FOUND_OR_PASSWORD_ERROR(30002, "服务器不存在或密码错误"),
    SERVER_NOT_FOUND_OR_USERNAME_ERROR(30003, "服务器不存在或用户名错误"),
    SERVER_NOT_FOUND_OR_PORT_ERROR(30004, "服务器不存在或端口错误"),
    SERVER_NOT_FOUND_OR_PRIVATE_KEY_ERROR(30005, "服务器不存在或私钥错误"),
    REMOTE_NOT_FOUND(30006, "远程服务器不存在"),
    REMOTE_NOT_FOUND_OR_PASSWORD_ERROR(30007, "远程服务器不存在或密码错误"),
    REMOTE_NOT_FOUND_OR_USERNAME_ERROR(30008, "远程服务器不存在或用户名错误"),
    REMOTE_NOT_FOUND_OR_PORT_ERROR(30009, "远程服务器不存在或端口错误"),
    REMOTE_NOT_FOUND_OR_PRIVATE_KEY_ERROR(30010, "远程服务器不存在或私钥错误"),
    SERVER_FOUND(30011, "服务器已存在")
}
