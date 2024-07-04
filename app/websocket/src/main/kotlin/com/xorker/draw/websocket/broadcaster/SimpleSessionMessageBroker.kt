package com.xorker.draw.websocket.broadcaster

import com.xorker.draw.room.RoomId
import com.xorker.draw.websocket.SessionId
import com.xorker.draw.websocket.SessionMessage
import com.xorker.draw.websocket.SessionMessageBroker
import com.xorker.draw.websocket.SessionUseCase
import com.xorker.draw.websocket.parser.WebSocketResponseParser
import org.springframework.stereotype.Component

@Component
class SimpleSessionMessageBroker(
    private val sessionUseCase: SessionUseCase,
    private val parser: WebSocketResponseParser,
) : SessionMessageBroker {

    override fun unicast(sessionId: SessionId, message: SessionMessage) {
        val session = sessionUseCase.getSession(sessionId) ?: return // TODO warn Logging
        val rawMessage = parser.parse(message)

        session.send(rawMessage)
    }

    override fun broadcast(roomId: RoomId, message: SessionMessage) {
        val rooms = sessionUseCase.getSessionsByRoomId(roomId) ?: return // TODO warn Logging
        val rawMessage = parser.parse(message)

        rooms.forEach {
            it.send(rawMessage)
        }
    }
}
