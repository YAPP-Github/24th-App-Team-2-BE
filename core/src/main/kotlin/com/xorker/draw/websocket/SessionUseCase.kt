package com.xorker.draw.websocket

import com.xorker.draw.user.UserId

interface SessionUseCase {
    fun registerSession(session: Session)
    fun unregisterSession(sessionId: SessionId)
    fun getSession(sessionId: SessionId): Session?
    fun getSession(userId: UserId): Session?
}
