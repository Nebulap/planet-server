package com.kai.planet.common.domain.entity.file

import com.mybatisflex.annotation.Column
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.KeyType
import com.mybatisflex.annotation.Table
import java.time.LocalDateTime

/**
 *
 * @since 10/7/2024 11:04 PM
 * @author 29002
 * @version 1.0.0
 */


@Table("t_files")
data class File(
    @Id(keyType = KeyType.Auto) val id: Long? = null,
    @Column("name") private val name: String = "",
    @Column("uuid") val uuid: String = "",
    @Column("path") val path: String = "",
    @Column("size") val size: Long = 0L,
    @Column("type") val type: String = "",
    @Column("created_at") val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column("is_deleted") val deleted: Boolean = false,
    @Column("owner") val owner: Long? = null
)

