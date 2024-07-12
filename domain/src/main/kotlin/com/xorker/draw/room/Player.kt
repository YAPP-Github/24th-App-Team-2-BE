package com.xorker.draw.room

import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.SessionId

abstract class Player(
    val userId: UserId,
    nickname: String,
    sessionId: SessionId,
) {
    var nickname: String = nickname
        private set
    var sessionId: SessionId? = sessionId
        private set

    fun connect(sessionId: SessionId) {
        this.sessionId = sessionId
    }

    fun disconnect() {
        this.sessionId = null
    }
}
