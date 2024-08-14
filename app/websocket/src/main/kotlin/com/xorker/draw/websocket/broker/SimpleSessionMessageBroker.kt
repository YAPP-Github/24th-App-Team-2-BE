package com.xorker.draw.websocket.broker

import com.xorker.draw.exception.InvalidBroadcastException
import com.xorker.draw.room.RoomRepository
import com.xorker.draw.websocket.BranchedBroadcastEvent
import com.xorker.draw.websocket.BroadcastEvent
import com.xorker.draw.websocket.RespectiveBroadcastEvent
import com.xorker.draw.websocket.SessionMessageBroker
import com.xorker.draw.websocket.SessionUseCase
import com.xorker.draw.websocket.UnicastEvent
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
    override fun unicast(event: UnicastEvent) {
        val userId = event.userId
        val session = sessionUseCase.getSession(userId)
        val response = parser.parse(event.message)

        session?.send(response)
    }

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

    @EventListener
    override fun broadcast(event: BranchedBroadcastEvent) {
        val roomId = event.roomId

        val branched = event.branched

        val message = event.message
        val branchedMessage = event.branchedMessage

        val response = parser.parse(message)
        val branchedResponse = parser.parse(branchedMessage)

        roomRepository.getRoom(roomId)?.let { room ->
            room.players.forEach { player ->
                val userId = player.userId

                sessionUseCase.getSession(userId)?.let {
                    if (userId in branched) {
                        it.send(branchedResponse)
                    } else {
                        it.send(response)
                    }
                }
            }
        }
    }

    @EventListener
    override fun broadcast(event: RespectiveBroadcastEvent) {
        val roomId = event.roomId

        val messages = event.messages

        roomRepository.getRoom(roomId)?.let { room ->
            room.players.forEach { player ->
                val userId = player.userId

                sessionUseCase.getSession(userId)?.let {
                    val response = parser.parse(messages[userId] ?: throw InvalidBroadcastException)

                    it.send(response)
                }
            }
        }
    }
}
