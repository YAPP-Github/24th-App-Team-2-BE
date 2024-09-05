package com.xorker.draw.webhook.discord.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "discord")
internal data class DiscordProperties(
    val randomMatchingUrl: String,
    val startGameUrl: String,
)
