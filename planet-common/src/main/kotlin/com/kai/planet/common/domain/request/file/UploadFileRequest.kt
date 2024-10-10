package com.kai.planet.common.domain.request.file

import org.springframework.web.multipart.MultipartFile

/**
 *
 * @since 10/7/2024 11:44 PM
 * @author 29002
 * @version 1.0.0
 */


class UploadFileRequest(
    val file: MultipartFile,
    val owner: Long?,
    val type: Int
)
