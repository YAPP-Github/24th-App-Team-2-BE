package com.xorker.draw.websocket.message.response.dto

import com.xorker.draw.websocket.ResponseAction
import com.xorker.draw.websocket.SessionMessage
import java.time.LocalDateTime

class MafiaPhaseReadyMessage(
    override val body: MafiaPhaseReadyBody,
) : SessionMessage {
    override val action = ResponseAction.GAME_READY
    override val status = SessionMessage.Status.OK
}

data class MafiaPhaseReadyBody(
    val startTime: LocalDateTime,
    val gameInfo: MafiaGameInfoMessage?,
)
