package com.xorker.draw.websocket.session

import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.Session
import com.xorker.draw.websocket.SessionId
import java.util.concurrent.ConcurrentHashMap
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

@Order(Ordered.HIGHEST_PRECEDENCE)
@Service
internal class SessionManager {
    private val sessionMap: ConcurrentHashMap<SessionId, Session> = ConcurrentHashMap()
    private val userIdMap: ConcurrentHashMap<UserId, Session> = ConcurrentHashMap()

    fun getSession(sessionId: SessionId): Session? {
        return sessionMap[sessionId]
    }

    fun getSession(userId: UserId): Session? {
        return userIdMap[userId]
    }

    fun registerSession(session: Session) {
        if (sessionMap.contains(session.id)) {
            // Init을 중복으로 호출 하면 기존 데이터를 Unregister 하고 Init 한다.
            unregisterSession(session.id)
        }

        sessionMap[session.id] = session
        userIdMap[session.user.id] = session
    }

    fun unregisterSession(sessionId: SessionId) {
        val session = sessionMap.remove(sessionId)
        userIdMap.remove(session?.user?.id)
    }
}
