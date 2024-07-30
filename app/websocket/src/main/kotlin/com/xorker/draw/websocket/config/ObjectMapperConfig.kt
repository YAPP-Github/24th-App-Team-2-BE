package com.xorker.draw.websocket.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfig {
    @Bean
    fun customObjectMapper(): ObjectMapper {
        val simpleModule = SimpleModule().apply {
            addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer())
        }

        return ObjectMapper().apply {
            registerModules(simpleModule)
            registerModules(KotlinModule.Builder().build())
        }
    }
}
