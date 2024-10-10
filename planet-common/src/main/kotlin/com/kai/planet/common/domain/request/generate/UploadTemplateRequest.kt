package com.kai.planet.common.domain.request.generate

/**
 *
 * @since 10/7/2024 10:11 PM
 * @author 29002
 * @version 1.0.0
 */


data class UploadTemplateRequest(
    val templateContent: String,
    val templateName: String
)
