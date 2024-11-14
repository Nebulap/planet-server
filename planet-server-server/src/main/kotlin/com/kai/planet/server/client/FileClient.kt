package com.kai.planet.server.client

import com.kai.planet.common.domain.entity.file.File
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 *
 * @since 2024/11/12 15:53
 * @author 29002
 * @version 1.0.0
 */

@FeignClient(name = "fileService", url = "http://localhost:8083")
interface FileClient {
    @GetMapping("/file/get-file-entity-by-uuid")
    fun getFileById(@RequestParam("uuid") uuid: String): File?
}
