package com.kai.planet.server.repository

import com.kai.planet.common.domain.entity.server.Server
import com.mybatisflex.core.BaseMapper
import org.apache.ibatis.annotations.Mapper

/**
 *
 * @since 2024/11/12 23:32
 * @author 29002
 * @version 1.0.0
 */


@Mapper
interface ServerRepository : BaseMapper<Server>
