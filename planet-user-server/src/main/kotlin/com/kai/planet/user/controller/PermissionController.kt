package com.kai.planet.user.controller

import com.kai.planet.user.service.PermissionService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @since 10/9/2024 11:24 AM
 * @author 29002
 * @version 1.0.0
 */


@RestController
@RequestMapping("/user/permission")
class PermissionController(
    private val permissionService: PermissionService
) {


}
