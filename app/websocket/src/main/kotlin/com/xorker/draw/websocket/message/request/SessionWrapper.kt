package com.xorker.draw.websocket.message.request

import com.xorker.draw.room.RoomId
import com.xorker.draw.user.User
import com.xorker.draw.websocket.Session
import com.xorker.draw.websocket.SessionId
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

class SessionWrapper(
    private val session: WebSocketSession,
    override val roomId: RoomId,
    override val user: User,
) : Session {
    override val id: SessionId = SessionId(session.id)

    override fun send(message: String) {
        session.sendMessage(TextMessage(message))
    }
}
