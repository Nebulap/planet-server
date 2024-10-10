package com.kai.planet.generate

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients

/**
 *
 * @since 10/7/2024 9:37 PM
 * @author 29002
 * @version 1.0.0
 */

@EnableFeignClients
@SpringBootApplication
open class PlanetGenerateApplication

fun main(args: Array<String>) {
    SpringApplication.run(PlanetGenerateApplication::class.java, *args)
}
