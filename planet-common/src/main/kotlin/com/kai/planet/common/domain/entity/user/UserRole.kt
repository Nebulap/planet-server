package com.kai.planet.common.domain.entity.user

import com.mybatisflex.annotation.Column
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.KeyType
import com.mybatisflex.annotation.Table

/**
 *
 * @since 10/8/2024 9:24 AM
 * @author 29002
 * @version 1.0.0
 */


@Table("t_users_roles")
data class UserRole(
    @Id(keyType = KeyType.Auto) val id: Long? = null,
    @Column("role_id") val roleId: Long,
    @Column("user_id") val userId: Long,
)
