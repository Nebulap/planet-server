package com.kai.planet.server

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients

/**
 *
 * @since 2024/11/10 18:44
 * @author 29002
 * @version 1.0.0
 */

@EnableFeignClients
@SpringBootApplication(scanBasePackages = ["com.kai.planet"])
class PlanetServerApplication

fun main(args: Array<String>) {
    SpringApplication.run(PlanetServerApplication::class.java, *args)
}
