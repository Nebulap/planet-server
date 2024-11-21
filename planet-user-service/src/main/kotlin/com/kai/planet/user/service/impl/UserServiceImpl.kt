package com.kai.planet.user.service.impl

import cn.dev33.satoken.stp.SaLoginModel
import cn.dev33.satoken.stp.StpUtil
import com.kai.planet.common.config.USER_CURRENT_ROLE_KEY
import com.kai.planet.common.domain.dto.user.UserInfoDTO
import com.kai.planet.common.domain.dto.user.UserSignInDTO
import com.kai.planet.common.domain.entity.user.Role
import com.kai.planet.common.domain.entity.user.User
import com.kai.planet.common.domain.entity.user.UserRole
import com.kai.planet.common.domain.request.email.SendSimpleMailRequest
import com.kai.planet.common.domain.request.user.SendCodeRequest
import com.kai.planet.common.domain.request.user.UserSignInRequest
import com.kai.planet.common.domain.request.user.UserSignUpRequest
import com.kai.planet.common.domain.request.user.ValidateCodeRequest
import com.kai.planet.common.exception.CustomException
import com.kai.planet.user.client.EmailClient
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
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *
 * @since 10/6/2024 11:10 PM
 * @author 29002
 * @version 1.0.0
 */

@Service
class UserServiceImpl(
    private val userMapper: UserMapper,
    private val emailClient: EmailClient,
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
        val key = "email:code:uuid:${request.email}"
        if (redisTemplate.hasKey(key)) {
            val uuid = redisTemplate.opsForValue().get(key) as String
            if (uuid != request.uuid) {
                throw CustomException(UserCustomExceptionCode.REGISTER_FAIL)
            }
        } else {
            throw CustomException(UserCustomExceptionCode.REGISTER_FAIL)
        }
        if (request.password != request.confirmPassword) {
            throw CustomException(UserCustomExceptionCode.PASSWORD_NOT_MATCH)
        }
        // Find user by username
        var user = findUser(request.username)
        if (user != null) {
            throw CustomException(UserCustomExceptionCode.USER_ALREADY_EXISTS)
        }

        val encodePw = BCryptPasswordEncoder().encode(request.password)
        user = User(null, request.username, encodePw, request.email,1, null)
        userMapper.insert(user)
        redisTemplate.delete(key)

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

    override fun sendCode(request: SendCodeRequest) {
        val user = filterOne<User> { User::email eq request.email }
        if (user != null) {
            throw CustomException(UserCustomExceptionCode.EMAIL_NOT_FOUND)
        }

        val code = generateVerificationCode()
        val key = "email:code:${request.email}"
        emailClient.sendSimpleMail(
            SendSimpleMailRequest(
                to = request.email,
                subject = "验证码",
                body = """
                    您的验证码是：$code
                    请在5分钟内完成验证。

                    此邮件来自 Planet (www.planet.cn)。如果您未发起此请求，请忽略本邮件。
        """.trimIndent()
            )
        )
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES)
    }

    override fun validateCode(request: ValidateCodeRequest): String {
        val key1 = "email:code:${request.email}"
        if (redisTemplate.hasKey(key1)) {
            val code = redisTemplate.opsForValue().get(key1) as String
            if (code != request.code) {
                throw CustomException(UserCustomExceptionCode.CODE_NOT_MATCH)
            }
        } else {
            throw CustomException(UserCustomExceptionCode.CODE_NOT_MATCH)
        }

        val uuid = UUID.randomUUID().toString()
        val key2 = "email:uuid:${request.email}"
        redisTemplate.opsForValue().set(key2,uuid)

        return uuid;
    }

    fun generateVerificationCode(): String {
        return (100000..999999).random().toString()
    }
}
