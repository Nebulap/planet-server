package com.kai.planet.generate.exception

import com.kai.planet.common.exception.CustomExceptionCode

/**
 *
 * @since 2024/11/12 15:45
 * @author 29002
 * @version 1.0.0
 */


enum class GenerateCustomExceptionCode(override val code: Int, override val msg: String):CustomExceptionCode {
    TEMPLATE_NOT_EXIST(10001, "模板不存在"),
    TEMPLATE_PARAM_NOT_EXIST(10002, "模板参数不存在"),
    TEMPLATE_PARAM_VALUE_NOT_EXIST(10003, "模板参数值不存在"),
    TEMPLATE_PARAM_VALUE_NOT_MATCH(10004, "模板参数值不匹配"),
    TEMPLATE_PARAM_VALUE_NOT_VALID(10005, "模板参数值不合法"),
    TEMPLATE_PARAM_VALUE_NOT_NULL(10006, "模板参数值不能为空")
}
