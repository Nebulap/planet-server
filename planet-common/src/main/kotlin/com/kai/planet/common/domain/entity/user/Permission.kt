package com.kai.planet.common.domain.entity.user

import com.kai.planet.common.constants.user.PermissionType
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.KeyType
import com.mybatisflex.annotation.Table

/**
 *
 * @since 10/8/2024 9:27 AM
 * @author 29002
 * @version 1.0.0
 */

@Table("t_permissions")
data class Permission(
    @Id(keyType = KeyType.Auto)
    val id: Long? = null,
    val parentId: Long? = null,
    val name: String,
    val code: String,
    val description: String? = null,
    val value: String? = null,
    val type: String = PermissionType.MENU
)
