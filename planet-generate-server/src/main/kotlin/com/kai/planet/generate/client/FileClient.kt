package com.kai.planet.generate.client

import com.kai.planet.common.domain.dto.file.FileResponseDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile

/**
 *
 * @since 2024/11/12 15:53
 * @author 29002
 * @version 1.0.0
 */

@FeignClient(name = "fileService", url = "http://localhost:8083")
interface FileClient {
    @PostMapping(value = ["/file/upload2"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadFile(@RequestPart("file") file: MultipartFile): FileResponseDTO
}
