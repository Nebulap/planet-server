package com.kai.planet.common.domain.entity.generate

import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table

/**
 *
 * @since 10/7/2024 10:22 PM
 * @author 29002
 * @version 1.0.0
 */


@Table("t_generate_template_menu")
data class GenerateTemplateMenu(
    @Id
    val templateId: Long? = null,
    val name: String,
    val type: String
)
