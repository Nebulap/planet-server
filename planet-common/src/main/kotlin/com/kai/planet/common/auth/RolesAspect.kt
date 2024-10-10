package com.kai.planet.common.auth

import cn.dev33.satoken.stp.StpUtil
import com.kai.planet.common.annotation.Roles
import com.kai.planet.common.exception.CustomException
import com.kai.planet.common.exception.GlobalExceptionCode
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import java.lang.reflect.Method

/**
 *
 * @since 10/8/2024 10:43 PM
 * @author 29002
 * @version 1.0.0
 */

@Aspect
@Component
class RolesAspect {
    @Around("@annotation(com.kai.planet.common.annotation.Roles) || @within(com.kai.planet.common.annotation.Roles)")
    @Throws(Throwable::class)
    fun around(joinPoint: ProceedingJoinPoint): Any? {
        // Get the method signature
        val signature = joinPoint.signature as MethodSignature
        val method: Method = signature.method

        // Get the @Roles annotation
        val rolesAnnotation = method.getAnnotation(Roles::class.java)
            ?: joinPoint.target.javaClass.getAnnotation(Roles::class.java)

        if (rolesAnnotation == null) {
            return joinPoint.proceed()
        }

        // Get the required roles
        val requiredRoles = rolesAnnotation.value
        val requiredRoleStrings = requiredRoles.map { it.name }.toTypedArray()

        // Check if the user has any of the required roles
        val hasAnyRole = StpUtil.hasRoleOr(*requiredRoleStrings)

        if (!hasAnyRole) {
            throw CustomException(GlobalExceptionCode.PERMISSION_ERROR)
        }

        // Has any of the required roles
        // Proceed with the method execution
        return joinPoint.proceed()
    }
}
