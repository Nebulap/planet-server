package com.kai.planet.file

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 *
 * @since 10/7/2024 11:12 PM
 * @author 29002
 * @version 1.0.0
 */

@SpringBootApplication(scanBasePackages = ["com.kai.planet"])
open class PlanetFileApplication

fun main(args: Array<String>) {
    SpringApplication.run(PlanetFileApplication::class.java, *args)
}
