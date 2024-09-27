package com.xorker.draw.webhook.discord

import com.xorker.draw.notify.NotifyRepository
import com.xorker.draw.notify.NotifyType
import com.xorker.draw.support.logging.logger
import com.xorker.draw.webhook.discord.config.DiscordProperties
import com.xorker.draw.webhook.discord.dto.DiscordEmbed
import com.xorker.draw.webhook.discord.dto.DiscordMessage
import org.springframework.stereotype.Component

@Component
internal class DiscordWebHookAdapter(
    private val discordClient: DiscordClient,
    private val discordProperties: DiscordProperties,
) : NotifyRepository {
    val logger = logger()

    override fun notifyMessage(notifyType: NotifyType) {
        try {
            when (notifyType) {
                is NotifyType.DiscordRandomMatchingNotifyType ->
                    discordClient.sendMessage(discordProperties.randomMatchingUrl, notifyType.toDiscordMessage()).block()

                is NotifyType.DiscordStartGameNotifyType ->
                    discordClient.sendMessage(discordProperties.startGameUrl, notifyType.toDiscordMessage()).block()
            }
        } catch (ex: Exception) {
            logger.warn(ex.message, ex)
        }
    }

    fun NotifyType.toDiscordMessage(): DiscordMessage = when (this) {
        is NotifyType.DiscordRandomMatchingNotifyType -> DiscordMessage(listOf(DiscordEmbed(this.message)))
        is NotifyType.DiscordStartGameNotifyType -> DiscordMessage(listOf(DiscordEmbed(this.message)))
    }
}
