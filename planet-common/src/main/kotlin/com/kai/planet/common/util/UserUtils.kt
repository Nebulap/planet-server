package com.kai.planet.common.util

import cn.dev33.satoken.stp.StpUtil

/**
 *
 * @since 10/8/2024 9:47 AM
 * @author 29002
 * @version 1.0.0
 */


object UserUtils {
    fun getUserId(): Long {
        return StpUtil.getLoginIdAsLong()
    }

//    fun hasRole(role: Int): Boolean {
//        val roles = StpUtil.c().stream().map { it.toInt() }.toList()
//        val a = StpUtil.
//    }
}
