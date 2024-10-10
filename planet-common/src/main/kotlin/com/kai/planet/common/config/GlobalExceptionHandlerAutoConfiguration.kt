package com.kai.planet.common.config

import com.kai.planet.common.exception.GlobalExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 *
 * @since 10/7/2024 3:22 AM
 * @author 29002
 * @version 1.0.0
 */

@Configuration
open class GlobalExceptionHandlerAutoConfiguration {

    @Bean
    open fun globalExceptionHandler(): GlobalExceptionHandler {
        println(">>>>>>>>>>>>>>>>>>>>>>>GlobalExceptionHandler injection success!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        return GlobalExceptionHandler()
    }
}
