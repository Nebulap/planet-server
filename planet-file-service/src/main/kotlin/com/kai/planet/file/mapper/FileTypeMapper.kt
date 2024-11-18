package com.kai.planet.file.mapper

import com.kai.planet.common.domain.entity.file.FileType
import com.mybatisflex.core.BaseMapper
import org.apache.ibatis.annotations.Mapper

/**
 *
 * @since 10/8/2024 9:16 AM
 * @author 29002
 * @version 1.0.0
 */

@Mapper
interface FileTypeMapper : BaseMapper<FileType> {
}
