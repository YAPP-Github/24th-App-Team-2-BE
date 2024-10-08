package com.xorker.draw.websocket.message.response.dto.game

import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.ResponseAction
import com.xorker.draw.websocket.SessionMessage
import java.time.LocalDateTime

data class MafiaGameTurnInfoMessage(
    override val body: MafiaGameTurnInfoBody,
) : SessionMessage {
    override val action: ResponseAction = ResponseAction.TURN_INFO
    override val status: SessionMessage.Status = SessionMessage.Status.OK
}

data class MafiaGameTurnInfoBody(
    val round: Int,
    val turn: Int,
    val startTurnTime: LocalDateTime,
    val currentTurnUser: UserId,
    val draw: List<Map<String, Any>>,
)
