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
    NOT_LOGIN_ERROR(5002, "请先登录"),
    NOT_ROLE_ERROR(5002, "没有相应的角色"),
    INPUT_STREAM_ERROR(5003, "输入流异常"),
    OUTPUT_STREAM_ERROR(5004, "输出流异常"),
    IO_ERROR(5005, "IO异常"),
}
