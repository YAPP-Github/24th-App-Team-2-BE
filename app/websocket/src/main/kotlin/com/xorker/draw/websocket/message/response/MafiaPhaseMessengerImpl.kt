package com.xorker.draw.websocket.message.response

import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.mafia.MafiaPhaseMessenger
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.SessionMessage
import com.xorker.draw.websocket.broker.WebSocketBroadcaster
import com.xorker.draw.websocket.message.response.dto.MafiaPhasePlayingBody
import com.xorker.draw.websocket.message.response.dto.MafiaPhasePlayingMessage
import com.xorker.draw.websocket.message.response.dto.MafiaPhaseWaitBody
import com.xorker.draw.websocket.message.response.dto.MafiaPhaseWaitMessage
import com.xorker.draw.websocket.message.response.dto.toResponse
import java.time.LocalDateTime
import org.springframework.stereotype.Component

@Component
class MafiaPhaseMessengerImpl(
    private val broadcaster: WebSocketBroadcaster,
) : MafiaPhaseMessenger {
    override fun unicastPhase(userId: UserId, gameInfo: MafiaGameInfo) {
        broadcaster.unicast(userId, gameInfo.generateMessage())
    }

    override fun broadcastPhase(gameInfo: MafiaGameInfo) {
        broadcaster.broadcast(gameInfo.room.id, gameInfo.generateMessage())
    }

    private fun MafiaGameInfo.generateMessage(): SessionMessage {
        return when (val phase = this.phase) {
            MafiaPhase.Wait -> MafiaPhaseWaitMessage(
                MafiaPhaseWaitBody(
                    room.id,
                    room.players.map { it.toResponse(room.owner) }.toList(),
                ),
            )

            is MafiaPhase.Ready -> TODO()
            is MafiaPhase.Playing -> MafiaPhasePlayingMessage(
                MafiaPhasePlayingBody(
                    round = phase.round,
                    turn = phase.turn,
                    startTurnTime = LocalDateTime.now(), // TOOD: 턴 시스템 도입 시 수정
                    draw = phase.drawData.take(phase.drawData.size - 1).map { it.second },
                ),
            )

            is MafiaPhase.Vote -> TODO()
            is MafiaPhase.InferAnswer -> TODO()
            is MafiaPhase.End -> TODO()
        }
    }
}
