package com.kai.planet.file.service.impl

import com.kai.planet.common.domain.dto.file.FileResponseDTO
import com.kai.planet.common.domain.entity.file.File
import com.kai.planet.common.domain.request.file.DeleteFileRequest
import com.kai.planet.common.domain.request.file.UploadFileRequest
import com.kai.planet.common.exception.CustomException
import com.kai.planet.file.exception.FileCustomExceptionCode
import com.kai.planet.file.mapper.FileMapper
import com.kai.planet.file.mapper.FileTypeMapper
import com.kai.planet.file.service.FileService
import com.mybatisflex.kotlin.extensions.db.filterOne
import com.mybatisflex.kotlin.extensions.kproperty.eq
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

/**
 *
 * @since 10/7/2024 11:14 PM
 * @author 29002
 * @version 1.0.0
 */

@Service
class FileServiceImpl(
    private val fileMapper: FileMapper,
    private val fileTypeMapper: FileTypeMapper
) : FileService {

    @Value("\${file.path}")
    lateinit var filePath: String

    override fun uploadFile(request: UploadFileRequest): FileResponseDTO {
        val type = fileTypeMapper.selectOneById(request.type)
            ?: throw CustomException(FileCustomExceptionCode.FILE_TYPE_NOT_FOUND)

        val requestFile = request.file!!
        val uuid = UUID.randomUUID().toString()
        val suffix = requestFile.originalFilename?.substringAfterLast(".") ?: ""
        val dirPath = request.temp?.let { getFullPath(type.name, LocalDate.now(), "") } ?: getTempPath()

        val file = java.io.File(dirPath)
        if (!file.exists()) {
            file.mkdirs()
        }
        requestFile.transferTo(java.io.File("$dirPath$uuid.$suffix"))

        val entity = File(
            name = requestFile.originalFilename ?: "unknown",
            path = "$dirPath$uuid.$suffix",
            size = requestFile.size,
            type = requestFile.contentType ?: "",
            owner = request.owner,
            uuid = uuid
        )

        fileMapper.insert(entity)
        println(FileResponseDTO(uuid))
        return FileResponseDTO(uuid)
    }

    override fun deleteFile(request: DeleteFileRequest) {
        val file: File = filterOne { File::uuid eq request.uuid } ?: throw CustomException(FileCustomExceptionCode.FILE_NOT_FOUND)
        java.io.File(file.path).delete()
    }

    override fun removeFile(request: DeleteFileRequest) {
        TODO("Not yet implemented")
    }

    override fun getFileEntityByUuid(uuid: String): File? {
        return filterOne { File::uuid eq uuid }
    }

    override fun getFileByUuid(uuid: String): java.io.File? {
        TODO("Not yet implemented")
    }

    override fun getFullPath(category: String, date: LocalDate, filename: String): String {
        val dateStr = date.toString().replace("-", "/")
        return "$filePath/$category/$dateStr/$filename"
    }


    fun getTempPath(): String {
        val dateStr = Date().toString().replace("-", "/")
        return "$filePath/temp/$dateStr"
    }
}
