package com.kai.planet.gateway.web

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange

@Component
class CustomCircuitBreakerFilterFactory : AbstractGatewayFilterFactory<CustomCircuitBreakerFilterFactory.Config>(Config::class.java) {

    private val log = LoggerFactory.getLogger(CustomCircuitBreakerFilterFactory::class.java)

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            chain.filter(exchange).doOnError { throwable ->
                logError(exchange, throwable, config)
            }
        }
    }

    private fun logError(exchange: ServerWebExchange, throwable: Throwable, config: Config) {
        val routeId = exchange.attributes[ServerWebExchange.LOG_ID_ATTRIBUTE] ?: "unknown"
        log.error("Route '$routeId' triggered fallback. Reason: {}", throwable.message)

        if (config.logStackTrace) {
            log.error("Stack Trace: ", throwable)
        }
    }

    data class Config(
        var logStackTrace: Boolean = false
    )
}
