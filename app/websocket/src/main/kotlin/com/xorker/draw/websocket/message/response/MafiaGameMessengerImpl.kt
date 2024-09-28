package com.xorker.draw.websocket.message.response

import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaGameMessenger
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.mafia.MafiaPhaseWithTurnList
import com.xorker.draw.mafia.MafiaReactionType
import com.xorker.draw.mafia.assertIs
import com.xorker.draw.room.RoomId
import com.xorker.draw.timer.TimerRepository
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.broker.WebSocketBroadcaster
import com.xorker.draw.websocket.message.response.mafia.MafiaGameAnswerBody
import com.xorker.draw.websocket.message.response.mafia.MafiaGameAnswerMessage
import com.xorker.draw.websocket.message.response.mafia.MafiaGameDrawMessage
import com.xorker.draw.websocket.message.response.mafia.MafiaGamePlayerListBody
import com.xorker.draw.websocket.message.response.mafia.MafiaGamePlayerListMessage
import com.xorker.draw.websocket.message.response.mafia.MafiaGameReactionBody
import com.xorker.draw.websocket.message.response.mafia.MafiaGameReactionMessage
import com.xorker.draw.websocket.message.response.mafia.MafiaGameTurnInfoBody
import com.xorker.draw.websocket.message.response.mafia.MafiaGameTurnInfoMessage
import com.xorker.draw.websocket.message.response.mafia.MafiaGameVoteStatusBody
import com.xorker.draw.websocket.message.response.mafia.MafiaGameVoteStatusMessage
import com.xorker.draw.websocket.message.response.mafia.MafiaRandomMatchingBody
import com.xorker.draw.websocket.message.response.mafia.MafiaRandomMatchingMessage
import com.xorker.draw.websocket.message.response.mafia.toResponse
import org.springframework.stereotype.Component

@Component
internal class MafiaGameMessengerImpl(
    private val broadcaster: WebSocketBroadcaster,
    private val timerRepository: TimerRepository,
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

        val message = MafiaGamePlayerListMessage(
            MafiaGamePlayerListBody(
                list.map { it.toResponse(gameInfo.room.owner) }.toList(),
            ),
        )

        broadcaster.broadcast(roomId, message)
    }

    override fun broadcastDraw(roomId: RoomId, data: Map<String, Any>) {
        val message = MafiaGameDrawMessage(data)

        broadcaster.broadcast(roomId, message)
    }

    override fun broadcastNextTurn(gameInfo: MafiaGameInfo) {
        val roomId = gameInfo.room.id

        val phase = gameInfo.phase
        assertIs<MafiaPhase.Playing>(phase)

        val body = MafiaGameTurnInfoBody(
            phase.round,
            phase.turn,
            timerRepository.getTimerStartTime(roomId),
            phase.turnList[phase.turn].userId,
            phase.drawData.map { it.second },
        )

        broadcaster.broadcast(roomId, MafiaGameTurnInfoMessage(body))
    }

    override fun broadcastVoteStatus(gameInfo: MafiaGameInfo) {
        val roomId = gameInfo.room.id

        val phase = gameInfo.phase
        assertIs<MafiaPhase.Vote>(phase)

        val message = MafiaGameVoteStatusMessage(
            MafiaGameVoteStatusBody(phase.players),
        )

        broadcaster.broadcast(roomId, message)
    }

    override fun broadcastAnswer(gameInfo: MafiaGameInfo, answer: String) {
        val roomId = gameInfo.room.id

        val phase = gameInfo.phase
        assertIs<MafiaPhase.InferAnswer>(phase)

        val message = MafiaGameAnswerMessage(
            MafiaGameAnswerBody(answer),
        )

        broadcaster.broadcast(roomId, message)
    }

    override fun unicastRandomMatching(userId: UserId) {
        val message = MafiaRandomMatchingMessage(
            MafiaRandomMatchingBody(),
        )

        broadcaster.unicast(userId, message)
    }

    override fun broadcastReaction(gameInfo: MafiaGameInfo, reaction: MafiaReactionType) {
        val roomId = gameInfo.room.id

        val phase = gameInfo.phase
        assertIs<MafiaPhase.Playing>(phase)

        val message = MafiaGameReactionMessage(
            MafiaGameReactionBody(
                reaction.name,
            ),
        )

        broadcaster.broadcast(roomId, message)
    }
}
