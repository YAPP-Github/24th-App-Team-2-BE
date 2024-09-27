package com.xorker.draw.config

import com.xorker.draw.mafia.dto.RedisMafiaGameInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
internal class RedisConfig {

    @Value("\${spring.data.redis.host}")
    lateinit var host: String

    @Value("\${spring.data.redis.password}")
    lateinit var password: String

    @Value("\${spring.data.redis.port}")
    lateinit var port: String

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val config = RedisStandaloneConfiguration(host, port.toInt())
        config.setPassword(password)

        return LettuceConnectionFactory(config)
    }

    @Bean
    fun redisTemplateWithObject(connectionFactory: RedisConnectionFactory): RedisTemplate<String, RedisMafiaGameInfo> {
        val template = RedisTemplate<String, RedisMafiaGameInfo>()

        template.connectionFactory = connectionFactory

        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = Jackson2JsonRedisSerializer(RedisMafiaGameInfo::class.java)

        return template
    }

    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, String> {
        val template = RedisTemplate<String, String>()

        template.connectionFactory = connectionFactory

        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = StringRedisSerializer()

        return template
    }
}
