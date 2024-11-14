package com.kai.planet.file.exception

import com.kai.planet.common.exception.CustomExceptionCode

/**
 *
 * @since 10/8/2024 9:42 AM
 * @author 29002
 * @version 1.0.0
 */


enum class FileCustomExceptionCode(override val code: Int, override val msg: String) : CustomExceptionCode {
    FILE_NOT_FOUND(20001, "文件不存在"),
    FILE_TYPE_NOT_FOUND(20002, "文件类型不存在")
}
