package com.xorker.draw.websocket

import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

private typealias SessionRoom = ConcurrentHashMap<String, MutableSet<WebSocketSession>>

@Component
class WebSocketSessionManager {
    private val sessionRoom: SessionRoom = ConcurrentHashMap()
    private val sessionMap: ConcurrentHashMap<String, WebSocketSession> = ConcurrentHashMap()

    fun startSession(roomId: String, session: WebSocketSession) {
        println("Start Session ${session.id}")

        sessionRoom.put(roomId, session)
        sessionMap[session.id] = session
    }

    fun endSession(roomId: String, session: WebSocketSession) {
        sessionRoom.remove(roomId, session)
        sessionMap.remove(session.id)
    }

    fun getSessions(roomId: String): Set<WebSocketSession>? {
        return sessionRoom[roomId]
    }
}

private fun SessionRoom.put(roomId: String, session: WebSocketSession) {
    val room = this.getOrPut(roomId) {
        Collections.synchronizedSet(mutableSetOf<WebSocketSession>())
    }

    room.add(session)
}

private fun SessionRoom.remove(roomId: String, session: WebSocketSession) {
    val room = this[roomId] ?: return
    room.remove(session)

    if (room.size == 0) {
        this.remove(roomId)
    }
}
