package com.kai.planet.common.config

import cn.dev33.satoken.config.SaTokenConfig
import cn.dev33.satoken.jwt.StpLogicJwtForSimple
import cn.dev33.satoken.stp.StpInterface
import cn.dev33.satoken.stp.StpLogic
import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.collection.ListUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

/**
 *
 * @since 10/11/2024 8:09 PM
 * @author 29002
 * @version 1.0.0
 */

@Component
class SaTokenConfiguration {

//    @Value("\${sa-token.jwt-secret-key}")
//    private lateinit var jwtSecretKey: String

    @Bean
    fun getStpLogicJwt(): StpLogic {
        return StpLogicJwtForSimple()
    }

    @Bean
    fun getStpInterface(): StpInterface {
        return object :StpInterface{
            override fun getPermissionList(p0: Any?, p1: String?): MutableList<String> {
                return mutableListOf()
            }

            override fun getRoleList(p0: Any?, p1: String?): MutableList<String> {
                val roleStr : Any? = StpUtil.getExtra(USER_CURRENT_ROLE_KEY)
                return ListUtil.toList(roleStr.toString())
            }

        }
    }


    @Bean
    @Primary
    fun saTokenConfigPrimary(): SaTokenConfig {
        return SaTokenConfig()
            .setTokenName(TOKEN_NAME)
            .setTokenPrefix(TOKEN_PREFIX)
            .setJwtSecretKey("asdas-dsad-sadasd-asd")
    }

}

const val USER_CURRENT_ROLE_KEY = "currentRole"
const val USER_ROLE_LIST_KEY = "roleList"
const val TOKEN_NAME = "Authorization"
const val TOKEN_PREFIX = "Bearer"

