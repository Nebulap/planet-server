package com.kai.planet.common.domain.entity.file

import com.mybatisflex.annotation.Column
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.KeyType
import com.mybatisflex.annotation.Table

/**
 *
 * @since 10/8/2024 9:11 AM
 * @author 29002
 * @version 1.0.0
 */

@Table("t_file_types")
data class FileType(
    @Id(keyType = KeyType.Auto, value = "id") val id: Long? = null,
    @Column("name") val name: String = "",
    @Column("description") val description: String? = null,
    @Column("role") val role: Int = -1
)
