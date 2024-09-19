package com.xorker.draw.websocket

import com.xorker.draw.user.User

@JvmInline
value class SessionId(val value: String)

interface WaitingQueueSession {
    val id: SessionId
    val user: User
    val locale: String
    fun send(message: String)
}
