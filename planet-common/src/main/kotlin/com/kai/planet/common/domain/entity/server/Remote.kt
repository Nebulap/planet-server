package com.kai.planet.common.domain.entity.server

import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.KeyType
import com.mybatisflex.annotation.Table

@Table("t_remote")
data class Remote(
    @Id(keyType = KeyType.Auto)
    val id: Long? = null, // 使用可空类型，以便可以根据需求为 null
    var serverId: Int,
    val host: String,
    val port: Int = 22, // 默认端口为 22
    val user: String ="",
    val password: String? ="",
    val identity: String? = "~/.ssh/id_rsa", // 默认值
    var passphrase: String = ""
)
