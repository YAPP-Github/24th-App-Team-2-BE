package com.xorker.draw.websocket

import com.xorker.draw.room.RoomId
import com.xorker.draw.user.User
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.dto.SessionInitializeRequest
import com.xorker.draw.websocket.dto.SessionWrapper
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession

@Service
class SessionFactory {
    fun create(session: WebSocketSession, request: SessionInitializeRequest): Session {
        return SessionWrapper(
            session,
            RoomId(request.roomId?.toUpperCase() ?: generateRoomId()),
            User(UserId(0), request.nickname), // TODO get User by AccessToken
        )
    }

    private fun generateRoomId(): String {
        // TODO: 중복 방지
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val value = (1..6)
            .map { charset.random() }
            .joinToString("")

        return value
    }
}
