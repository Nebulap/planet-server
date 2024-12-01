package com.kai.planet.user.repository

import com.kai.planet.common.domain.entity.user.UserProfile
import com.mybatisflex.core.BaseMapper
import org.apache.ibatis.annotations.Mapper

/**
 *
 * @since 2024/11/30 下午5:28
 * @author 29002
 * @version 1.0.0
 */

@Mapper
interface UserProfileRepository : BaseMapper<UserProfile> {
}
