package com.xorker.draw.websocket.message.response.mafia

import com.xorker.draw.websocket.message.response.ResponseAction
import com.xorker.draw.websocket.message.response.SessionMessage
import java.time.LocalDateTime

data class MafiaPhaseReadyMessage(
    override val body: MafiaPhaseReadyBody,
) : SessionMessage {
    override val action = ResponseAction.PHASE_READY
    override val status = SessionMessage.Status.OK
}

data class MafiaPhaseReadyBody(
    val startTime: LocalDateTime,
    val mafiaGameInfo: MafiaGameInfoMessage,
)
