package com.xorker.draw.websocket

import com.xorker.draw.user.UserId
import java.util.concurrent.ConcurrentHashMap
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

@Order(Ordered.HIGHEST_PRECEDENCE)
@Service
internal class SessionService : SessionUseCase, SessionEventListener {
    private val sessionMap: ConcurrentHashMap<SessionId, Session> = ConcurrentHashMap()
    private val userIdMap: ConcurrentHashMap<UserId, Session> = ConcurrentHashMap()

    override fun registerSession(session: Session) {
        if (sessionMap.contains(session.id)) {
            // Init을 중복으로 호출 하면 기존 데이터를 Unregister 하고 Init 한다.
            unregisterSession(session.id)
        }

        sessionMap[session.id] = session
        userIdMap[session.user.id] = session
    }

    override fun unregisterSession(sessionId: SessionId) {
        val session = sessionMap.remove(sessionId)
        userIdMap.remove(session?.user?.id)
    }

    override fun getSession(sessionId: SessionId): Session? {
        return sessionMap[sessionId]
    }

    override fun getSession(userId: UserId): Session? {
        return userIdMap[userId]
    }

    override fun connectSession(session: Session, request: SessionInitializeRequest) {
        registerSession(session)
    }

    override fun disconnectSession(session: Session) {
        unregisterSession(session.id)
    }

    override fun exitSession(session: Session) {
        unregisterSession(session.id)
    }
}
