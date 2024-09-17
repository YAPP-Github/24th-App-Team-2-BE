package com.xorker.draw.webhook.discord.config

import com.xorker.draw.webhook.discord.DiscordClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@EnableConfigurationProperties(DiscordProperties::class)
@Configuration
internal class DiscordClientConfig {

    @Bean
    fun webClient(): WebClient = WebClient.create("https://discord.com")

    @Bean
    fun discordClient(): DiscordClient {
        val factory = HttpServiceProxyFactory.builder()
            .exchangeAdapter(WebClientAdapter.create(webClient()))
            .build()

        return factory.createClient(DiscordClient::class.java)
    }
}
