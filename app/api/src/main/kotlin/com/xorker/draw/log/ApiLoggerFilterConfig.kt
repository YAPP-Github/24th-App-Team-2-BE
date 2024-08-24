package com.xorker.draw.log

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered

@Configuration
class ApiLoggerFilterConfig {
    @Bean
    fun apiLogFilter(filter: ApiLoggingFilter): FilterRegistrationBean<ApiLoggingFilter> {
        val bean = FilterRegistrationBean<ApiLoggingFilter>()

        bean.filter = filter
        bean.order = Ordered.HIGHEST_PRECEDENCE
        bean.urlPatterns = listOf("/api/*")

        return bean
    }
}
