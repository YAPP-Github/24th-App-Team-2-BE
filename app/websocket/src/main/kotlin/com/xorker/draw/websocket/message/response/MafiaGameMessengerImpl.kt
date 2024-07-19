package com.xorker.draw.websocket.message.response

import com.xorker.draw.exception.InvalidMafiaGamePlayingPhaseStatusException
import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaGameMessenger
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.mafia.MafiaPlayer
import com.xorker.draw.room.Room
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.BranchedBroadcastEvent
import com.xorker.draw.websocket.BroadcastEvent
import com.xorker.draw.websocket.RespectiveBroadcastEvent
import com.xorker.draw.websocket.SessionMessage
import com.xorker.draw.websocket.broker.WebSocketBroadcaster
import com.xorker.draw.websocket.message.response.dto.MafiaGameInfoBody
import com.xorker.draw.websocket.message.response.dto.MafiaGameInfoMessage
import com.xorker.draw.websocket.message.response.dto.MafiaGameReadyBody
import com.xorker.draw.websocket.message.response.dto.MafiaGameReadyMessage
import com.xorker.draw.websocket.message.response.dto.MafiaPlayerListBody
import com.xorker.draw.websocket.message.response.dto.MafiaPlayerListMessage
import com.xorker.draw.websocket.message.response.dto.toResponse
import org.springframework.stereotype.Component

@Component
class MafiaGameMessengerImpl(
    private val broadcaster: WebSocketBroadcaster,
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

        broadcaster.publishBroadcastEvent(event)
    }

    override fun broadcastGameInfo(mafiaGameInfo: MafiaGameInfo) {
        val roomId = mafiaGameInfo.room.id

        val phase = mafiaGameInfo.phase as? MafiaPhase.Playing ?: throw InvalidMafiaGamePlayingPhaseStatusException

        val mafia = phase.mafiaPlayer
        val keyword = phase.keyword

        val gameOption = mafiaGameInfo.gameOption

        val message = MafiaGameInfoMessage(
            MafiaGameInfoBody(
                category = keyword.category,
                answer = keyword.answer,
                gameOption = gameOption.toResponse(),
            ),
        )

        val branchedMessage = MafiaGameInfoMessage(
            MafiaGameInfoBody(
                isMafia = true,
                category = keyword.category,
                answer = keyword.answer,
                gameOption = gameOption.toResponse(),
            ),
        )

        val branched = setOf(mafia.userId)

        val event = BranchedBroadcastEvent(
            roomId = roomId,
            branched = branched,
            message = message,
            branchedMessage = branchedMessage,
        )

        broadcaster.publishBranchedBroadcastEvent(event)
    }

    override fun broadcastGameReady(mafiaGameInfo: MafiaGameInfo) {
        val roomId = mafiaGameInfo.room.id

        val phase = mafiaGameInfo.phase as? MafiaPhase.Playing ?: throw InvalidMafiaGamePlayingPhaseStatusException

        val turnList = phase.turnList

        val messages = mutableMapOf<UserId, SessionMessage>()

        turnList.forEachIndexed { i, player ->
            val message = MafiaGameReadyMessage(
                MafiaGameReadyBody(
                    turn = i + 1,
                    player = player.toResponse(),
                ),
            )
            messages[player.userId] = message
        }

        val event = RespectiveBroadcastEvent(
            roomId = roomId,
            messages = messages,
        )

        broadcaster.publishRespectiveBroadcastEvent(event)
    }
}
