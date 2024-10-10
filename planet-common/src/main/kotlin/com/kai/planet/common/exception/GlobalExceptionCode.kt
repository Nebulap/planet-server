package com.kai.planet.common.exception

/**
 *
 * @since 10/7/2024 1:21 AM
 * @author 29002
 * @version 1.0.0
 */


enum class GlobalExceptionCode(override val code: Int, override val msg: String) : CustomExceptionCode {
    OK(0, "成功"),

    PARAMETER_ERROR(1000, "参数错误"),
    SERVICE_ERROR(5000, "服务异常"),
    PERMISSION_ERROR(5001, "你没有权限访问此资源"),
}
