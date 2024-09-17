package com.xorker.draw.i18n

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource

@Configuration
class MessageConfig {
    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ResourceBundleMessageSource()
        messageSource.setDefaultEncoding("UTF-8")
        messageSource.setBasename("i18n/messages")
        return messageSource
    }
}
