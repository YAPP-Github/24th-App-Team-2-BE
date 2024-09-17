package com.xorker.draw.websocket

import com.xorker.draw.user.User

interface WaitingQueueSessionEventListener {
    fun connectSession(user: User, locale: String)
    fun exitSession(user: User, locale: String)
}
