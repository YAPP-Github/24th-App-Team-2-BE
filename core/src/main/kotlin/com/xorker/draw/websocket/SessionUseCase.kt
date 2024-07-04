package com.xorker.draw.websocket

interface SessionUseCase {
    fun registerSession(session: Session)
    fun unregisterSession(sessionId: SessionId)
}
