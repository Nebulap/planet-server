package com.kai.planet.file.controller

import cn.dev33.satoken.annotation.SaIgnore
import com.kai.planet.common.domain.dto.file.FileResponseDTO
import com.kai.planet.common.domain.entity.file.File
import com.kai.planet.common.domain.request.file.UploadFileRequest
import com.kai.planet.file.service.FileService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

/**
 *
 * @since 10/7/2024 11:11 PM
 * @author 29002
 * @version 1.0.0
 */


@SaIgnore
@RestController
@RequestMapping("/file")
class FileController(
    private val fileService: FileService
) {

    @PostMapping(value = ["/upload"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(request: UploadFileRequest): FileResponseDTO {
        return fileService.uploadFile(request)
    }

    @PostMapping(value = ["/upload2"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload2(@RequestParam("file") file: MultipartFile): FileResponseDTO {
        val request = UploadFileRequest(file, 1, 1, true)
        return fileService.uploadFile(request)
    }

    @GetMapping("/get-file-entity-by-uuid")
    fun getFileById(@RequestParam("uuid") uuid: String): File? {
        return fileService.getFileEntityByUuid(uuid)
    }
}
