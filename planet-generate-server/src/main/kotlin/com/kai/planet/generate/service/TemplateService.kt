package com.kai.planet.generate.service

import com.kai.planet.common.domain.dto.file.FileResponseDTO
import com.kai.planet.common.domain.request.generate.GenerateFileRequest
import java.io.OutputStream

/**
 *
 * @since 10/7/2024 10:02 PM
 * @author 29002
 * @version 1.0.0
 */


interface TemplateService {
    fun generateFile(request: GenerateFileRequest): FileResponseDTO
    fun generateFileStream(request: GenerateFileRequest): OutputStream
}
