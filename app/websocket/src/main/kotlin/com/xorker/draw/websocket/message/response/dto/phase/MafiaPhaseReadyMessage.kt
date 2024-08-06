package com.xorker.draw.websocket.message.response.dto.phase

import com.xorker.draw.websocket.ResponseAction
import com.xorker.draw.websocket.SessionMessage
import com.xorker.draw.websocket.message.response.dto.MafiaGameInfoMessage
import java.time.LocalDateTime

class MafiaPhaseReadyMessage(
    override val body: MafiaPhaseReadyBody,
) : SessionMessage {
    override val action = ResponseAction.PHASE_READY
    override val status = SessionMessage.Status.OK
}

data class MafiaPhaseReadyBody(
    val startTime: LocalDateTime,
    val mafiaGameInfo: MafiaGameInfoMessage,
)
