package com.xorker.draw.websocket.message.response.dto.phase

import com.xorker.draw.websocket.message.response.ResponseAction
import com.xorker.draw.websocket.message.response.SessionMessage
import java.time.LocalDateTime

data class MafiaPhasePlayingMessage(
    override val body: MafiaPhasePlayingBody,
) : SessionMessage {
    override val action: ResponseAction = ResponseAction.PHASE_PLAYING
    override val status: SessionMessage.Status = SessionMessage.Status.OK
}

data class MafiaPhasePlayingBody(
    val round: Int,
    val turn: Int,
    val startTurnTime: LocalDateTime,
    val draw: List<Map<String, Any>>,
    val currentDraw: Map<String, Any>,
    val mafiaGameInfo: MafiaGameInfoMessage,
)
