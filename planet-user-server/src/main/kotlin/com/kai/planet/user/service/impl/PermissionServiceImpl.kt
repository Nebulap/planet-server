package com.kai.planet.user.service.impl

import com.kai.planet.common.constants.user.RoleEnum
import com.kai.planet.common.domain.entity.user.Permission
import com.kai.planet.common.domain.entity.user.Role
import com.kai.planet.common.domain.entity.user.User
import com.kai.planet.common.domain.entity.user.UserRole
import com.kai.planet.user.service.PermissionService
import com.mybatisflex.kotlin.extensions.db.deleteWith
import com.mybatisflex.kotlin.extensions.db.filterOne
import com.mybatisflex.kotlin.extensions.db.insert
import com.mybatisflex.kotlin.extensions.kproperty.eq
import org.springframework.stereotype.Service


/**
 *
 * @since 10/9/2024 1:51 AM
 * @author 29002
 * @version 1.0.0
 */


@Service
class PermissionServiceImpl : PermissionService {
    override fun getPermissionByUserId(userId: Long): List<Permission> {
        //TODO
        throw NotImplementedError()
    }

    override fun getRoleByUserIdAndRoleId(userId: Long, roleId: Long): UserRole? =
        filterOne {
            (UserRole::userId eq userId).and(UserRole::roleId eq roleId)
        }

    override fun getCurrentRole(userId: Long): Role? {
        val user: User = filterOne { User::id eq userId } ?: return null
        return filterOne { Role::id eq user.role }
    }

    override fun getRoleByEnum(roleEnum: RoleEnum): Role? = filterOne { Role::name eq roleEnum.name }

    /**
     * Add role to user
     * @param userId user id
     * @param roleEnum role enum
     * @return true if success
     */
    override fun addRole(userId: Long, roleEnum: RoleEnum): Boolean {
        val role: Role = getRoleByEnum(roleEnum) ?: return false
//        val role: Role = getRoleByEnum(roleEnum) ?: throw CustomException(UserCustomExceptionCode.USER_ROLE_NOT_EXISTS)
        val userRole: UserRole? = getRoleByUserIdAndRoleId(userId, role.id!!)

        if (userRole != null) return false

        // Create user and role relation
        return insert(UserRole(null, role.id!!, userId)) > 0
    }

    override fun removeRole(userId: Long, roleEnum: RoleEnum): Boolean {
        val role: Role = getRoleByEnum(roleEnum) ?: return false
//        val role: Role = getRoleByEnum(roleEnum)  ?: throw CustomException(UserCustomExceptionCode.USER_ROLE_NOT_EXISTS)
        return deleteWith<UserRole> {
            (UserRole::userId eq userId).and(UserRole::roleId eq role.id!!)
        } > 0
    }


}
