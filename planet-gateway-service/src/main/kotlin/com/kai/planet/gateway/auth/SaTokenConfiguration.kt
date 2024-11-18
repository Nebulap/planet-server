package com.kai.planet.gateway.auth

import cn.dev33.satoken.reactor.filter.SaReactorFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 *
 * @since 10/10/2024 10:54 PM
 * @author 29002
 * @version 1.0.0
 */


@Configuration("gatewaySaTokenConfiguration")
open class SaTokenConfiguration {

    @Bean
    open fun getSaReactorFilter(): SaReactorFilter? {
        return MySaReactorFilter()
    }
}
