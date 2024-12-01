package com.kai.planet.user.repository

import com.kai.planet.common.domain.entity.user.User
import com.mybatisflex.core.BaseMapper
import org.apache.ibatis.annotations.Mapper

/**
 *
 * @since 10/6/2024 11:05 PM
 * @author 29002
 * @version 1.0.0
 */

@Mapper
interface UserRepository : BaseMapper<User> {
}
