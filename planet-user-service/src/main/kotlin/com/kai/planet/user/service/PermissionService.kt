package com.kai.planet.user.service

import com.kai.planet.common.constants.user.RoleEnum
import com.kai.planet.common.domain.entity.user.Permission
import com.kai.planet.common.domain.entity.user.Role
import com.kai.planet.common.domain.entity.user.UserRole

/**
 *
 * @since 10/9/2024 1:49 AM
 * @author 29002
 * @version 1.0.0
 */


interface PermissionService {
    fun getPermissionByUserId(userId: Long): List<Permission>
    fun getRoleByUserIdAndRoleId(userId: Long,roleId: Long): UserRole?
    fun getCurrentRole(userId: Long): Role?


    fun addRole(userId: Long, roleEnum: RoleEnum): Boolean
    fun removeRole(userId: Long, roleEnum: RoleEnum): Boolean
    fun getRoleByEnum(roleEnum: RoleEnum): Role?
}
