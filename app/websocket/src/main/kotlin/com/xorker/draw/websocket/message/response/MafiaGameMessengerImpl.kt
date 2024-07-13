package com.xorker.draw.websocket.message.response

import com.xorker.draw.mafia.MafiaGameMessenger
import com.xorker.draw.mafia.MafiaPlayer
import com.xorker.draw.room.Room
import com.xorker.draw.websocket.BroadcastEvent
import com.xorker.draw.websocket.event.EventPublisher
import com.xorker.draw.websocket.message.response.dto.MafiaPlayerListBody
import com.xorker.draw.websocket.message.response.dto.MafiaPlayerListMessage
import com.xorker.draw.websocket.message.response.dto.toResponse
import org.springframework.stereotype.Component

@Component
class MafiaGameMessengerImpl(
    private val eventPublisher: EventPublisher,
) : MafiaGameMessenger {
    override fun broadcastPlayerList(room: Room<MafiaPlayer>) {
        val roomId = room.id

        val message = MafiaPlayerListMessage(
            MafiaPlayerListBody(
                roomId,
                room.players.map { it.toResponse() }.toList(),
            ),
        )

        val event = BroadcastEvent(roomId, message)

        eventPublisher.publishBroadcastEvent(event)
    }
}
