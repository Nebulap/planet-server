package com.kai.planet.common.config

import com.kai.planet.common.auth.RolesAspect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.logging.Logger

/**
 *
 * @since 10/8/2024 11:38 PM
 * @author 29002
 * @version 1.0.0
 */

@Configuration
open class RolesAspectAutoConfiguration {

    private val logger: Logger = Logger.getLogger(RolesAspectAutoConfiguration::class.java.name)

    @Bean
    open fun rolesAspect(): RolesAspect {
        logger.info("RolesAspect injection success!")
        return RolesAspect()
    }
}
