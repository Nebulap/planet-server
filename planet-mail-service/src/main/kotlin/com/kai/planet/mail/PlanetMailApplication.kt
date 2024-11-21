package com.kai.planet.mail

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 *
 * @since 2024/11/18 20:36
 * @author 29002
 * @version 1.0.0
 */


@SpringBootApplication(scanBasePackages = ["com.kai.planet"])
open class PlanetMailApplication {
}

fun main(args: Array<String>) {
    SpringApplication.run(PlanetMailApplication::class.java, *args)
}
