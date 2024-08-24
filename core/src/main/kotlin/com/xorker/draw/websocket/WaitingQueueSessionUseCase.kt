package com.xorker.draw.websocket

import com.xorker.draw.user.UserId

interface WaitingQueueSessionUseCase {
    fun registerSession(session: WaitingQueueSession)
    fun unregisterSession(sessionId: SessionId)
    fun getSession(sessionId: SessionId): WaitingQueueSession?
    fun getSession(userId: UserId): WaitingQueueSession?
}
