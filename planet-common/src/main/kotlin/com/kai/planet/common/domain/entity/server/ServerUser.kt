package com.kai.planet.common.domain.entity.server

import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.KeyType
import com.mybatisflex.annotation.Table

/**
 *
 * @since 2024/11/10 22:32
 * @author 29002
 * @version 1.0.0
 */


@Table("t_server_user")
data class ServerUser(
    @Id(keyType = KeyType.Auto)
    val id: Long? = null,
    val serverId: Int,
    val userId: Long,
)
