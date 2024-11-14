package com.kai.planet.generate.service.impl

import com.kai.planet.common.domain.dto.file.FileResponseDTO
import com.kai.planet.common.domain.entity.generate.GenerateTemplate
import com.kai.planet.common.domain.request.generate.GenerateFileRequest
import com.kai.planet.common.exception.CustomException
import com.kai.planet.generate.client.FileClient
import com.kai.planet.generate.exception.GenerateCustomExceptionCode
import com.kai.planet.generate.service.TemplateService
import com.mybatisflex.kotlin.extensions.db.filterOne
import com.mybatisflex.kotlin.extensions.kproperty.eq
import freemarker.template.Configuration
import org.springframework.beans.factory.annotation.Value
import org.springframework.mock.web.MockMultipartFile
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileWriter
import java.io.OutputStream
import java.util.*


/**
 *
 * @since 10/7/2024 10:02 PM
 * @author 29002
 * @version 1.0.0
 */

@Service
class TemplateServiceImpl(
    private val fileClient: FileClient
) : TemplateService {

    @Value("\${spring.freemarker.template-loader-path}")
    lateinit var templatePath: String
    override fun generateFile(request: GenerateFileRequest): FileResponseDTO {

        val templateEntity: GenerateTemplate = filterOne { (GenerateTemplate::id eq request.templateId) } ?: throw CustomException(GenerateCustomExceptionCode.TEMPLATE_NOT_EXIST)

        val cfg = Configuration(Configuration.VERSION_2_3_33)
        cfg.setDirectoryForTemplateLoading(File(templatePath))
        cfg.defaultEncoding = Charsets.UTF_8.toString()
        val template = cfg.getTemplate(templateEntity.templateName)

        val uuid = UUID.randomUUID().toString()
        val filename = uuid + "." + templateEntity.templateName.split(".")[1]


        FileWriter(File("$templatePath/$filename")).use { out ->
            template.process(request.root, out)
        }

        val file = MockMultipartFile(filename, filename, null, File("$templatePath/$filename").inputStream())
        val uploadFileResponse = fileClient.uploadFile(file)

        return uploadFileResponse
    }

    override fun generateFileStream(request: GenerateFileRequest): OutputStream {
        TODO("Not yet implemented")
    }

}
