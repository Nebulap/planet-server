package com.kai.planet.common.config

import cn.dev33.satoken.stp.StpUtil
import com.kai.planet.common.constants.http.CustomHttpHeaders
import com.kai.planet.common.feign.CustomFeignClient
import feign.RequestInterceptor
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 *
 * @since 10/9/2024 6:21 PM
 * @author 29002
 * @version 1.0.0
 */

@Configuration
open class FeignClientAutoConfiguration {

    @PostConstruct
    fun init() {
        println(">>>>>>>>>>>>>>>>>>>>FeignClientAutoConfiguration injection success!")
    }

    @Bean
    open fun feignRequestInterceptor(): RequestInterceptor {
        return RequestInterceptor { template ->
            template.header(CustomHttpHeaders.X_INTERNAL_REQUEST, true.toString())
            template.header(TOKEN_NAME,StpUtil.getTokenValue())
        }
    }

    @Bean
    open fun customFeignClient(): CustomFeignClient {
        return CustomFeignClient()
    }
}
