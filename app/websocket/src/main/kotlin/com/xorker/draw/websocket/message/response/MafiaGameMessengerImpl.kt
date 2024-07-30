package com.xorker.draw.websocket.message.response

import com.xorker.draw.exception.InvalidMafiaGamePlayingPhaseStatusException
import com.xorker.draw.exception.InvalidMafiaGameVotePhaseStatusException
import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaGameMessenger
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.mafia.MafiaPhaseWithTurnList
import com.xorker.draw.mafia.assertIs
import com.xorker.draw.room.RoomId
import com.xorker.draw.websocket.BranchedBroadcastEvent
import com.xorker.draw.websocket.BroadcastEvent
import com.xorker.draw.websocket.broker.WebSocketBroadcaster
import com.xorker.draw.websocket.message.response.dto.MafiaAnswerBody
import com.xorker.draw.websocket.message.response.dto.MafiaAnswerMessage
import com.xorker.draw.websocket.message.response.dto.MafiaGameDrawMessage
import com.xorker.draw.websocket.message.response.dto.MafiaGameTurnInfoBody
import com.xorker.draw.websocket.message.response.dto.MafiaGameTurnInfoMessage
import com.xorker.draw.websocket.message.response.dto.MafiaPlayerListBody
import com.xorker.draw.websocket.message.response.dto.MafiaPlayerListMessage
import com.xorker.draw.websocket.message.response.dto.MafiaPlayerTurnListBody
import com.xorker.draw.websocket.message.response.dto.MafiaPlayerTurnListMessage
import com.xorker.draw.websocket.message.response.dto.MafiaVoteStatusBody
import com.xorker.draw.websocket.message.response.dto.MafiaVoteStatusMessage
import com.xorker.draw.websocket.message.response.dto.toResponse
import org.springframework.stereotype.Component

@Component
class MafiaGameMessengerImpl(
    private val broadcaster: WebSocketBroadcaster,
) : MafiaGameMessenger {

    override fun broadcastPlayerList(gameInfo: MafiaGameInfo) {
        val roomId = gameInfo.room.id
        val phase = gameInfo.phase

        val list =
            if (phase is MafiaPhaseWithTurnList) {
                phase.turnList
            } else {
                gameInfo.room.players
            }

        val message = MafiaPlayerListMessage(
            MafiaPlayerListBody(
                list.map { it.toResponse(gameInfo.room.owner) }.toList(),
            ),
        )

        val event = BroadcastEvent(roomId, message)

        broadcaster.publishBroadcastEvent(event)
    }

    override fun broadcastGameInfo(mafiaGameInfo: MafiaGameInfo) {
        TODO("Not yet implemented")
    }

    override fun broadcastGameReady(mafiaGameInfo: MafiaGameInfo) {
        TODO("Not yet implemented")
    }

    override fun broadcastPlayerTurnList(mafiaGameInfo: MafiaGameInfo) {
        val room = mafiaGameInfo.room
        val roomId = room.id

        val phase = mafiaGameInfo.phase as? MafiaPhase.Playing ?: throw InvalidMafiaGamePlayingPhaseStatusException
        val turn = phase.turn
        val turnList = phase.turnList

        val currentTurnPlayer = turnList[turn]

        val mafiaPlayerResponses = turnList
            .map {
                it.toResponse(mafiaGameInfo.room.owner)
            }.toList()

        val message = MafiaPlayerTurnListMessage(
            MafiaPlayerTurnListBody(
                turn = turn,
                players = mafiaPlayerResponses,
            ),
        )

        val branchedMessage = MafiaPlayerTurnListMessage(
            MafiaPlayerTurnListBody(
                turn = turn,
                isMyTurn = true,
                players = mafiaPlayerResponses,
            ),
        )

        val branched = setOf(currentTurnPlayer.userId)

        val event = BranchedBroadcastEvent(
            roomId = roomId,
            branched = branched,
            message = message,
            branchedMessage = branchedMessage,
        )

        broadcaster.publishBranchedBroadcastEvent(event)
    }

    override fun broadcastDraw(roomId: RoomId, data: Map<String, Any>) {
        val message = MafiaGameDrawMessage(data)
        broadcaster.broadcast(roomId, message)
    }

    override fun broadcastNextTurn(gameInfo: MafiaGameInfo) {
        val phase = gameInfo.phase
        assertIs<MafiaPhase.Playing>(phase)
        val body = MafiaGameTurnInfoBody(
            phase.round,
            phase.turn,
            phase.timerJob.startTime,
            phase.turnList[phase.turn].userId,
        )
        broadcaster.broadcast(gameInfo.room.id, MafiaGameTurnInfoMessage(body))
    }

    override fun broadcastVoteStatus(mafiaGameInfo: MafiaGameInfo) {
        val roomId = mafiaGameInfo.room.id

        val phase = mafiaGameInfo.phase as? MafiaPhase.Vote ?: throw InvalidMafiaGameVotePhaseStatusException

        val message = MafiaVoteStatusMessage(
            MafiaVoteStatusBody(phase.players),
        )

        val event = BroadcastEvent(roomId, message)

        broadcaster.publishBroadcastEvent(event)
    }

    override fun broadcastAnswer(gameInfo: MafiaGameInfo, answer: String) {
        val roomId = gameInfo.room.id

        val message = MafiaAnswerMessage(MafiaAnswerBody(answer))

        broadcaster.broadcast(roomId, message)
    }
}
