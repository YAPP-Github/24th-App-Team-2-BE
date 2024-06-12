package com.xorker.xpr.oauth.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@EnableFeignClients(basePackages = ["com.xorker.xpr"])
@Configuration
internal class OpenFeignConfig
