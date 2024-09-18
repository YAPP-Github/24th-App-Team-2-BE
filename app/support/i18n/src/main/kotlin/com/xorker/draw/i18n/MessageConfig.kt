package com.xorker.draw.i18n

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver


@Configuration
class MessageConfig {

    @Bean
    fun messageSource(): MessageSource {
        val resolver: ResourcePatternResolver = PathMatchingResourcePatternResolver()
        val resources = resolver.getResources("classpath*:i18n/*")

        val basename = resources
            .mapNotNull { it.filename }
            .map { "i18n/${it.substring(0, it.lastIndexOf('_'))}" }
            .distinct()
            .toTypedArray()

        val messageSource = ResourceBundleMessageSource()
        messageSource.setDefaultEncoding("UTF-8")
        messageSource.setBasenames(*basename)

        return messageSource
    }
}
