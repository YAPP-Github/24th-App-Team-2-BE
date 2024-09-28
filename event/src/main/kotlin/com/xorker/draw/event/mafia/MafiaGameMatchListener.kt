package com.xorker.draw.event.mafia

import com.xorker.draw.user.User

interface MafiaGameMatchListener {
    fun startRandomMatch(user: User, locale: String, isLastPlayer: Boolean)
}
