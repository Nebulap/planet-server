package com.kai.planet.common.domain.entity.generate

import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.KeyType
import com.mybatisflex.annotation.Table

/**
 *
 * @since 10/7/2024 10:16 PM
 * @author 29002
 * @version 1.0.0
 */


@Table("t_generate_template_param")
data class GenerateTemplateParam(
    @Id(keyType = KeyType.Auto)
    val templateId: Long? = null,
    val key: String,
    val valueType: String,
    val descriptor: String?,
    val order: Int
)
