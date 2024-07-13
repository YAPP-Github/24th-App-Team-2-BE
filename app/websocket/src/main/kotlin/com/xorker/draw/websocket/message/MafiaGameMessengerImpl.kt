package com.xorker.draw.websocket.message

import com.xorker.draw.mafia.MafiaGameMessenger
import com.xorker.draw.mafia.MafiaPlayer
import com.xorker.draw.room.Room
import com.xorker.draw.websocket.message.dto.MafiaPlayerListBody
import com.xorker.draw.websocket.message.dto.MafiaPlayerListMessage
import com.xorker.draw.websocket.message.dto.toResponse
import org.springframework.stereotype.Component

@Component
class MafiaGameMessengerImpl : MafiaGameMessenger {
    override fun broadcastPlayerList(room: Room<MafiaPlayer>) {
        val roomId = room.id

        val message = MafiaPlayerListMessage(
            MafiaPlayerListBody(
                roomId,
                room.players.map { it.toResponse() }.toList(),
            ),
        )

        // TODO: 메시지 전송
    }
}
