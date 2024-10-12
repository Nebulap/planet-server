package com.kai.planet.common.config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.util.logging.Logger

/**
 *
 * @since 10/9/2024 1:35 AM
 * @author 29002
 * @version 1.0.0
 */


@Configuration
open class MyRedisAutoConfiguration {

    private val logger: Logger = Logger.getLogger(RolesAspectAutoConfiguration::class.java.name)

    @Bean
    open fun redisConnectionFactory(): RedisConnectionFactory {

        val redisConfig = RedisStandaloneConfiguration().apply {
            hostName = "localhost"
            port = 6379
            database = 2
        }

        val poolConfig = GenericObjectPoolConfig<Any>().apply {
            maxIdle = 16
            maxTotal = 32
            minIdle = 8
        }

        val clientConfig = LettucePoolingClientConfiguration.builder()
            .poolConfig(poolConfig)
            .build()

        return LettuceConnectionFactory(redisConfig, clientConfig)
    }

    @Bean(name = ["redisTemplate"])
    open fun getRedisTemplate(factory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.connectionFactory = factory

        val stringRedisSerializer = StringRedisSerializer()

        // Set key serializer
        redisTemplate.keySerializer = stringRedisSerializer

        // Set up Jackson2JsonRedisSerializer for value serialization
        val objectMapper = ObjectMapper().apply {
            setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
            activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
            )
        }
        val jackson2JsonRedisSerializer = Jackson2JsonRedisSerializer(objectMapper,Any::class.java)

        // Set value and hash value serializers
        redisTemplate.valueSerializer = jackson2JsonRedisSerializer
        redisTemplate.hashKeySerializer = stringRedisSerializer
        redisTemplate.hashValueSerializer = jackson2JsonRedisSerializer

        redisTemplate.afterPropertiesSet()

        logger.info("RedisTemplate injection success!")
        return redisTemplate
    }

}
