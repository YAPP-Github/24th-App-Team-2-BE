package com.xorker.draw.websocket.session

import com.xorker.draw.user.User
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

internal class SessionWrapper(
    private val session: WebSocketSession,
    override val user: User,
    override val locale: String,
) : Session {
    override val id: SessionId = SessionId(session.id)

    override fun send(message: String) {
        synchronized(session) {
            session.sendMessage(TextMessage(message))
        }
    }
}
