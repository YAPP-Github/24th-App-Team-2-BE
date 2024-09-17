package com.xorker.draw.webhook.discord

import com.xorker.draw.webhook.discord.dto.DiscordMessage
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.PostExchange
import reactor.core.publisher.Mono

internal interface DiscordClient {
    @PostExchange("/api/webhooks/{path}")
    fun sendMessage(
        @PathVariable("path") path: String,
        @RequestBody discordMessage: DiscordMessage,
    ): Mono<Void>
}
