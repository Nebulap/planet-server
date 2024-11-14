package com.kai.planet.common.domain.entity.user

import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.KeyType
import com.mybatisflex.annotation.Table

/**
 *
 * @since 2024/11/12 23:23
 * @author 29002
 * @version 1.0.0
 */

@Table("t_test")
data class Test2(
    @Id(keyType = KeyType.Auto)
     var id: Long? = null,
     var test: Int? = null
)
