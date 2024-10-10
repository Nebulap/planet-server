package com.kai.planet.common.domain.entity.generate

import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.KeyType
import com.mybatisflex.annotation.Table

/**
 *
 * @since 10/7/2024 10:14 PM
 * @author 29002
 * @version 1.0.0
 */

@Table("t_generate_template")
data class GenerateTemplate(
    @Id(keyType = KeyType.Auto)
    val id: Long? = null,
    val templateName: String,
    val templatePath: String,
)
