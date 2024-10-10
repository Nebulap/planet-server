package com.kai.planet.common.annotation

import com.kai.planet.common.constants.user.RoleEnum

/**
 *
 * @since 10/8/2024 10:36 PM
 * @author 29002
 * @version 1.0.0
 */

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Roles(
    vararg val value: RoleEnum
)
