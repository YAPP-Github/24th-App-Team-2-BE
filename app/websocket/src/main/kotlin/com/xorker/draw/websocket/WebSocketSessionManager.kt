package com.xorker.draw.websocket

import com.xorker.draw.room.RoomId
import com.xorker.draw.websocket.dto.WebSocketSessionWrapper
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

private typealias SessionRoom = ConcurrentHashMap<RoomId, MutableSet<WebSocketSessionWrapper>>

@Component
class WebSocketSessionManager {
    private val sessionRoom: SessionRoom = ConcurrentHashMap()
    private val sessionMap = ConcurrentHashMap<String, WebSocketSessionWrapper>()

    fun startSession(session: WebSocketSessionWrapper) {
        println("Start Session ${session.id}")

        sessionRoom.put(session.roomId, session)
        sessionMap[session.id] = session
    }

    fun endSession(session: WebSocketSession) {
        val wrapper = sessionMap[session.id] ?: return
        sessionRoom.remove(wrapper)
        sessionMap.remove(session.id)
    }

    fun getSessions(roomId: RoomId): Set<WebSocketSessionWrapper>? {
        return sessionRoom[roomId]
    }

    fun generateRoomId(): RoomId {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val value = (1..6)
            .map { charset.random() }
            .joinToString("")

        return RoomId(value)
    }
}

private fun SessionRoom.put(roomId: RoomId, session: WebSocketSessionWrapper) {
    val room = this.getOrPut(roomId) {
        Collections.synchronizedSet(mutableSetOf<WebSocketSessionWrapper>())
    }

    room.add(session)
}

private fun SessionRoom.remove(session: WebSocketSessionWrapper) {
    val room = this[session.roomId] ?: return
    room.remove(session)

    if (room.size == 0) {
        this.remove(session.roomId)
    }
}
