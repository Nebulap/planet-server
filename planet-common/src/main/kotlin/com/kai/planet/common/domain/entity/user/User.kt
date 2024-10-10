package com.kai.planet.common.domain.entity.user

import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.KeyType
import com.mybatisflex.annotation.Table
import java.time.LocalDateTime

/**
 *
 * @since 10/6/2024 10:11 PM
 * @author 29002
 * @version 1.0.0
 */

@Table("t_users")
data class User (
    @Id(keyType = KeyType.Auto) val id: Long?,
    val username: String,
    val password: String,
    val role: Int,
    val createTime: LocalDateTime?
)
