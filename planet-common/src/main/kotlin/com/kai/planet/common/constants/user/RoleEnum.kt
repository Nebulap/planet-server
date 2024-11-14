package com.kai.planet.common.constants.user

/**
 *
 * @since 10/8/2024 10:38 PM
 * @author 29002
 * @version 1.0.0
 */


enum class RoleEnum(
    val level: Int
) {
    ADMIN(99),
    USER(1),
    GUEST(0)
}
