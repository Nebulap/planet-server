package com.kai.planet.gateway.auth

import cn.dev33.satoken.stp.StpInterface
import org.springframework.stereotype.Component

/**
 *
 * @since 10/9/2024 1:29 AM
 * @author 29002
 * @version 1.0.0
 */

@Component
class StpInterfaceImpl : StpInterface {

    override fun getPermissionList(p0: Any?, p1: String?): MutableList<String> {
        TODO("Not yet implemented")
    }

    override fun getRoleList(p0: Any?, p1: String?): MutableList<String> {
        TODO("Not yet implemented")
    }
}
