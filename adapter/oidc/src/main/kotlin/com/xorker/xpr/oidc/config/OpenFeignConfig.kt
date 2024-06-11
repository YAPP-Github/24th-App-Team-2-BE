package com.xorker.xpr.oidc.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@EnableFeignClients(basePackages = ["com.xorker.xpr"])
@Configuration
internal class OpenFeignConfig
