package com.kai.planet.common.constants.user

/**
 *
 * @since 10/9/2024 1:55 AM
 * @author 29002
 * @version 1.0.0
 */


object UserRedisKey {
    private const val USER_INFO_KEY = "user:info:"



    fun getUserInfoKey(userId: Long): String = "$USER_INFO_KEY$userId"
}
