package com.xorker.draw.webhook.discord

import com.xorker.draw.event.mafia.MafiaGameMatchListener
import com.xorker.draw.notify.NotifyType
import com.xorker.draw.user.User
import org.springframework.stereotype.Component

@Component
internal class DeveloperNotification(
    private val adapter: DiscordWebHookAdapter,
) : MafiaGameMatchListener {
    override fun startRandomMatch(user: User, locale: String, isLastPlayer: Boolean) {
        adapter.notifyMessage(NotifyType.DiscordRandomMatchingNotifyType(user.name, locale))
    }
}
