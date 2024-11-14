package com.kai.planet.common.domain.request.generate

/**
 *
 * @since 2024/11/12 15:05
 * @author 29002
 * @version 1.0.0
 */


data class GenerateFileRequest(
    val templateId: Long,
    val root: Map<String, Any>,
)
