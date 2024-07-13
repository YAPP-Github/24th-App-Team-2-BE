package com.xorker.draw.websocket.broadcaster

import com.xorker.draw.room.RoomId
import com.xorker.draw.room.RoomRepository
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.SessionId
import com.xorker.draw.websocket.SessionMessage
import com.xorker.draw.websocket.SessionMessageBroker
import com.xorker.draw.websocket.SessionUseCase
import com.xorker.draw.websocket.parser.WebSocketResponseParser
import org.springframework.stereotype.Component

@Component
class SimpleSessionMessageBroker(
    private val sessionUseCase: SessionUseCase,
    private val roomRepository: RoomRepository,
    private val parser: WebSocketResponseParser,
) : SessionMessageBroker {

    override fun unicast(userId: UserId, message: SessionMessage) {
        val session = sessionUseCase.getSession(userId) ?: return // TODO warn Logging
        val rawMessage = parser.parse(message)

        session.send(rawMessage)
    }

    override fun broadcast(roomId: RoomId, message: SessionMessage) {
        val sessions = roomRepository.getRoom(roomId) ?: return // TODO warn Logging
        val rawMessage = parser.parse(message)

        sessions.players.forEach {
            val session = sessionUseCase.getSession(it.userId)
            session?.send(rawMessage)
        }
    }
}
