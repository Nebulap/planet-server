package com.kai.planet.user.repository

import com.kai.planet.common.domain.entity.user.UserRole
import com.mybatisflex.core.BaseMapper
import org.apache.ibatis.annotations.Mapper

/**
 *
 * @since 10/9/2024 2:10 AM
 * @author 29002
 * @version 1.0.0
 */

@Mapper
interface UserRoleRepository : BaseMapper<UserRole> {
}
