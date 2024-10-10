package com.kai.planet.file.mapper

import com.kai.planet.common.domain.entity.file.File
import com.mybatisflex.core.BaseMapper
import org.apache.ibatis.annotations.Mapper

/**
 *
 * @since 10/7/2024 11:25 PM
 * @author 29002
 * @version 1.0.0
 */

@Mapper
interface FileMapper : BaseMapper<File> {
}
