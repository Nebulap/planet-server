package com.kai.planet.file.service

import com.kai.planet.common.domain.dto.file.FileResponseDTO
import com.kai.planet.common.domain.entity.file.File
import com.kai.planet.common.domain.request.file.DeleteFileRequest
import com.kai.planet.common.domain.request.file.UploadFileRequest
import java.time.LocalDate

/**
 *
 * @since 10/7/2024 11:14 PM
 * @author 29002
 * @version 1.0.0
 */


interface FileService {
    fun uploadFile(request: UploadFileRequest): FileResponseDTO
    fun deleteFile(request: DeleteFileRequest)
    fun removeFile(request: DeleteFileRequest)
    fun getFileEntityByUuid(uuid: String): File?
    fun getFileByUuid(uuid: String): java.io.File?
    fun getFullPath(category: String, date: LocalDate, filename: String) : String
}
