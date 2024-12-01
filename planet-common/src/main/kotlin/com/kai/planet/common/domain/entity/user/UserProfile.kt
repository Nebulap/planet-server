package com.kai.planet.common.domain.entity.user

import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.KeyType
import com.mybatisflex.annotation.Table

/**
 *
 * @since 2024/11/30 下午5:25
 * @author 29002
 * @version 1.0.0
 */


@Table("t_user_profile")
data class UserProfile(
    @Id(keyType = KeyType.None)
    val id: Long,
    val avatar: String = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2TSgUFImVS1nQjtDONCs5OnHnADICqxiF1w&s",
    val nickname: String = "Big Big Wolf",
)
