package com.kai.planet.file.controller

import com.kai.planet.common.domain.request.file.UploadFileRequest
import com.kai.planet.file.service.FileService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @since 10/7/2024 11:11 PM
 * @author 29002
 * @version 1.0.0
 */


@RestController
@RequestMapping("/file")
class FileController(
    private val fileService: FileService
) {

    @PostMapping("/upload")
    fun upload(request: UploadFileRequest) =  fileService.uploadFile(request)
}
