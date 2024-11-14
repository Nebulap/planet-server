package com.kai.planet.common.domain.request.file

import org.springframework.web.multipart.MultipartFile

/**
 *
 * @since 10/7/2024 11:44 PM
 * @author 29002
 * @version 1.0.0
 */


data class UploadFileRequest(
    val file: MultipartFile? = null,
    val owner: Long? = -1,
    val type: Int = -1,
    val temp: Boolean? = false  // 是否是临时文件
)
