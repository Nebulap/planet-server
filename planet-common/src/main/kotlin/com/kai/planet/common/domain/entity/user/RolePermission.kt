package com.kai.planet.common.domain.entity.user

import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.KeyType
import com.mybatisflex.annotation.Table

/**
 *
 * @since 10/8/2024 9:36 AM
 * @author 29002
 * @version 1.0.0
 */


@Table("t_roles_permissions")
data class RolePermission(
    @Id(keyType = KeyType.Auto)
    val id: Long? = null,
    val permissionId: Long,
    val roleId: Long
)
