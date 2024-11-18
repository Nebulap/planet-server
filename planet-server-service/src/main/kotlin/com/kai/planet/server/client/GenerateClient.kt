package com.kai.planet.server.client

import com.kai.planet.common.domain.dto.file.FileResponseDTO
import com.kai.planet.common.domain.request.generate.GenerateFileRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

/**
 *
 * @since 2024/11/12 21:55
 * @author 29002
 * @version 1.0.0
 */


@FeignClient(name = "generateService", url = "http://localhost:8082")
interface GenerateClient {
    @PostMapping("/generate/temp/create")
    fun generateFile(@RequestBody request: GenerateFileRequest): FileResponseDTO
}
