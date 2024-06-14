package com.xorker.xpr.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Configuration

@SecurityScheme(
    name = "Authorization",
    type = SecuritySchemeType.HTTP,
    `in` = SecuritySchemeIn.HEADER,
    bearerFormat = "JWT",
    scheme = "Bearer",
)
@OpenAPIDefinition(
    info = Info(
        title = "XPR API Docs",
        version = "v1.0.0",
    ),
)
@Configuration
internal class SwaggerConfig
