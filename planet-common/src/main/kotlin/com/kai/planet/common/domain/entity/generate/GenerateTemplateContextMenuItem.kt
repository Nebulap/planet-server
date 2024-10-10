package com.kai.planet.common.domain.entity.generate

import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table

/**
 *
 * @since 10/7/2024 10:40 PM
 * @author 29002
 * @version 1.0.0
 */


@Table("t_generate_template_context_menu_item")
data class GenerateTemplateContextMenuItem(
    @Id
    val templateId: Long,
    val type: String,
    val label: String,
    val action: String,
    val icon: String? = null,
    val order: Int
)
