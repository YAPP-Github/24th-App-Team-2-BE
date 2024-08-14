package com.xorker.draw.config

import com.xorker.draw.support.jwt.JwtProvider
import com.xorker.draw.support.jwt.JwtSecretKey
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class JwtConfig {
    @Value("\${jwt.secret}")
    lateinit var secret: String

    @Bean
    fun jwtProvider(): JwtProvider {
        return JwtProvider()
    }

    @Bean
    fun jwtSecretKey(): JwtSecretKey {
        return JwtSecretKey(secret)
    }
}
