package com.kai.planet.user.service.impl

import cn.dev33.satoken.stp.SaLoginModel
import cn.dev33.satoken.stp.StpUtil
import com.kai.planet.common.config.USER_CURRENT_ROLE_KEY
import com.kai.planet.common.domain.dto.user.UserInfoDTO
import com.kai.planet.common.domain.dto.user.UserSignInDTO
import com.kai.planet.common.domain.entity.user.Role
import com.kai.planet.common.domain.entity.user.User
import com.kai.planet.common.domain.entity.user.UserRole
import com.kai.planet.common.domain.request.user.UserSignInRequest
import com.kai.planet.common.domain.request.user.UserSignUpRequest
import com.kai.planet.common.exception.CustomException
import com.kai.planet.user.exception.UserCustomExceptionCode
import com.kai.planet.user.mapper.UserMapper
import com.kai.planet.user.service.PermissionService
import com.kai.planet.user.service.UserService
import com.mybatisflex.kotlin.extensions.db.filterOne
import com.mybatisflex.kotlin.extensions.db.query
import com.mybatisflex.kotlin.extensions.kproperty.eq
import com.mybatisflex.kotlin.extensions.wrapper.select
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

/**
 *
 * @since 10/6/2024 11:10 PM
 * @author 29002
 * @version 1.0.0
 */

@Service
class UserServiceImpl(
    private val userMapper: UserMapper,
    private val redisTemplate: RedisTemplate<String, Any>,

    private val permissionService: PermissionService,
) : UserService {

    override fun signIn(request: UserSignInRequest): UserSignInDTO {

        // Find user by username
        val user = findUser(request.username) ?: throw CustomException(UserCustomExceptionCode.USER_NOT_FOUND)

        // Check if the provided password matches the hashed password stored in the database
        val passwordMatch = BCryptPasswordEncoder().matches(request.password, user.password)
        if (!passwordMatch) {
            throw CustomException(UserCustomExceptionCode.USER_NOT_FOUND_OR_PASSWORD_ERROR)
        }

        val info = this.getUserInfo(user.id!!)

        val loginModel = SaLoginModel()
            .setExtra(USER_CURRENT_ROLE_KEY, info.currentRole!!.code)

        // Login user and return the token
        StpUtil.login(user.id!!, loginModel)
        return UserSignInDTO(StpUtil.getTokenValue())
    }

    override fun signUp(request: UserSignUpRequest): UserSignInDTO {
        // Find user by username
        var user = findUser(request.username)
        if (user != null) {
            throw CustomException(UserCustomExceptionCode.USER_ALREADY_EXISTS)
        }

        val encodePw = BCryptPasswordEncoder().encode(request.password)
        user = User(null, request.username, encodePw, 1, null)
        userMapper.insert(user)

        // Add ADMIN role
        permissionService.addRole(user.id!!, com.kai.planet.common.constants.user.RoleEnum.ADMIN)

        val info = this.getUserInfo(user.id!!)

        val loginModel = SaLoginModel()
            .setExtra(USER_CURRENT_ROLE_KEY, info.currentRole!!.code)

        // Login user and return the token
        StpUtil.login(user.id!!, loginModel)
        return UserSignInDTO(StpUtil.getTokenValue())
    }

    /**
     *  Find user by username
     *  @param username username
     *  @return user
     */
    override fun findUser(username: String): User? = filterOne { User::username eq username }

    override fun findUser(userId: Long): User? = filterOne { User::id eq userId }

//    override fun updateUserInfoInRedis(userId: Long): UserInfoDTO {
//        val key = UserRedisKey.getUserInfoKey(userId)
//
////        redisTemplate.opsForValue().set(key, JSON.toJSONString(userInfoRedisDTO))
////
////        return userInfoRedisDTO;
//
//        TODO()
//    }

    override fun getUserInfo(userId: Long): UserInfoDTO {
        val user: User = this.findUser(userId) ?: throw CustomException(UserCustomExceptionCode.USER_NOT_FOUND)
        val role: Role = filterOne { Role::id eq user.role } ?: throw CustomException(UserCustomExceptionCode.USER_ROLE_NOT_EXISTS)
        val roles: List<Role> = query {
//            select(UserRole::roleId, UserRole::userId).`as`("t1")
            select(Role::class)
            from(UserRole::class.java)
            leftJoin(Role::class.java).on(UserRole::roleId, Role::id)
            where(UserRole::userId eq userId)
        }

        return UserInfoDTO(
            id = userId,
            username = user.username,
            roles = roles,
            currentRole = role
        )
    }

}
