package com.xorker.draw.support.auth.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@Configuration
internal class SecurityConfig {
    @Bean
    fun filterChain(
        http: HttpSecurity,
        securityExceptionHandler: SecurityExceptionHandler,
        tokenAuthenticationFilter: TokenAuthenticationFilter,
    ): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .exceptionHandling {
                it.authenticationEntryPoint(securityExceptionHandler)
                it.accessDeniedHandler(securityExceptionHandler)
            }
            .authorizeHttpRequests {
                it.requestMatchers(*WHITE_LIST)
                    .permitAll()
                it.anyRequest()
                    .permitAll()
            }
            .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    companion object {
        private val WHITE_LIST = arrayOf(
            "/api/v1/auth/**",
        )
    }
}
