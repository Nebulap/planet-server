package com.kai.planet.gateway

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 *
 * @since 10/7/2024 2:00 AM
 * @author 29002
 * @version 1.0.0
 */

@SpringBootApplication
open class PlanetGatewayApplication

fun main(args: Array<String>) {
            SpringApplication.run(PlanetGatewayApplication::class.java, *args)
}
