package com.xorker.draw.websocket.broker

import com.xorker.draw.room.RoomRepository
import com.xorker.draw.websocket.BroadcastEvent
import com.xorker.draw.websocket.SessionMessageBroker
import com.xorker.draw.websocket.SessionUseCase
import com.xorker.draw.websocket.parser.WebSocketResponseParser
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SimpleSessionMessageBroker(
    private val sessionUseCase: SessionUseCase,
    private val roomRepository: RoomRepository,
    private val parser: WebSocketResponseParser,
) : SessionMessageBroker {

    @EventListener
    override fun broadcast(event: BroadcastEvent) {
        val roomId = event.roomId

        val message = event.message

        val response = parser.parse(message)

        roomRepository.getRoom(roomId)?.let { room ->
            room.players.forEach { player ->
                val userId = player.userId

                sessionUseCase.getSession(userId)?.send(response)
            }
        }
    }
}
