package com.xorker.draw.firebase

import com.xorker.draw.event.mafia.MafiaGameMatchListener
import com.xorker.draw.user.User
import org.springframework.stereotype.Component

@Component
internal class UserNotification(
    private val fcmService: FcmService,
) : MafiaGameMatchListener {
    override fun startRandomMatch(user: User, locale: String, isLastPlayer: Boolean) {
        if (isLastPlayer.not()) {
            fcmService.quickStart(locale, user.name)
        }
    }
}
