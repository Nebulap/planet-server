package com.kai.planet.gateway.exception

import com.kai.planet.common.exception.CustomExceptionCode

/**
 *
 * @since 10/7/2024 5:51 PM
 * @author 29002
 * @version 1.0.0
 */


enum class ServerCustomExceptionCode(override val code: Int, override val msg: String) : CustomExceptionCode {
    USER_SERVICE_ERROR(503, "用户服务不可用，请稍后再试"),
    FILE_SERVICE_ERROR(503, "文件服务不可用，请稍后再试"),
    GENERATE_SERVICE_ERROR(503, "代码生成服务不可用，请稍后再试"),
    SERVER_SERVICE_ERROR(503, "服务器服务不可用，请稍后再试"),
}
