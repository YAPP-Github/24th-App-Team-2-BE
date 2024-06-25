package com.xorker.draw.oauth.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@EnableFeignClients(basePackages = ["com.xorker.draw"])
@Configuration
internal class OpenFeignConfig
