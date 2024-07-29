package com.xorker.draw.websocket.message.response

import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.mafia.MafiaPhaseMessenger
import com.xorker.draw.mafia.assertIs
import com.xorker.draw.websocket.BroadcastEvent
import com.xorker.draw.websocket.broker.WebSocketBroadcaster
import com.xorker.draw.websocket.message.response.dto.MafiaPhasePlayingBody
import com.xorker.draw.websocket.message.response.dto.MafiaPhasePlayingMessage
import java.time.LocalDateTime
import org.springframework.stereotype.Component

@Component
class MafiaPhaseMessengerImpl(
    private val broadcaster: WebSocketBroadcaster,
) : MafiaPhaseMessenger {
    override fun broadcastPlaying(gameInfo: MafiaGameInfo) {
        val phase = gameInfo.phase
        assertIs<MafiaPhase.Playing>(phase)

        val event = BroadcastEvent(
            gameInfo.room.id,
            MafiaPhasePlayingMessage(
                MafiaPhasePlayingBody(
                    round = phase.round,
                    turn = phase.turn,
                    startTurnTime = LocalDateTime.now(), // TOOD: 턴 시스템 도입 시 수정
                    draw = phase.drawData.take(phase.drawData.size - 1).map { it.second },
                ),
            ),
        )

        broadcaster.publishBroadcastEvent(event)
    }
}
