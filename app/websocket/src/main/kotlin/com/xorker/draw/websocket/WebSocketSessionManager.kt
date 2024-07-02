package com.xorker.draw.websocket

import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class WebSocketSessionManager {
    private val sessionMap = ConcurrentHashMap<String, WebSocketSession>()

    fun startSession(session: WebSocketSession) {
        println("Start Session ${session.id}")
        sessionMap[session.id] = session
    }

    fun endSession(session: WebSocketSession) {
        sessionMap.remove(session.id)
    }

    fun getSession(sessionId: String): WebSocketSession {
        return sessionMap[sessionId] ?: throw RuntimeException()
    }
}
