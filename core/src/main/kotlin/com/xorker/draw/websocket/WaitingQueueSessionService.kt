package com.xorker.draw.websocket

import com.xorker.draw.user.UserId
import java.util.concurrent.ConcurrentHashMap
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

@Order(Ordered.HIGHEST_PRECEDENCE)
@Service
internal class WaitingQueueSessionService : WaitingQueueSessionUseCase, WaitingQueueSessionEventListener {
    private val sessions: ConcurrentHashMap<SessionId, WaitingQueueSession> = ConcurrentHashMap()
    private val userIds: ConcurrentHashMap<UserId, WaitingQueueSession> = ConcurrentHashMap()

    override fun registerSession(session: WaitingQueueSession) {
        if (sessions.contains(session.id)) {
            unregisterSession(session.id)
        }

        val user = session.user

        sessions[session.id] = session
        userIds[user.id] = session
    }

    override fun unregisterSession(sessionId: SessionId) {
        val session = sessions.remove(sessionId)
        session?.let {
            val user = it.user

            userIds.remove(user.id)
        }
    }

    override fun getSession(sessionId: SessionId): WaitingQueueSession? {
        return sessions[sessionId]
    }

    override fun getSession(userId: UserId): WaitingQueueSession? {
        return userIds[userId]
    }

    override fun connectSession(session: WaitingQueueSession) {
        registerSession(session)
    }

    override fun exitSession(session: WaitingQueueSession) {
        unregisterSession(session.id)
    }
}
