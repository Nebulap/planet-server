package com.kai.planet.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 *
 * @since 10/6/2024 7:40 PM
 * @author 29002
 * @version 1.0.0
 */


@SpringBootApplication
@EnableFeignClients(basePackages = "com.kai.planet.user.client")
public class PlanetUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlanetUserApplication.class, args);
    }
}
