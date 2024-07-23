package com.xorker.draw.websocket.message.response.dto

import com.xorker.draw.websocket.ResponseAction
import com.xorker.draw.websocket.SessionMessage

class MafiaGameReadyMessage(
    override val body: MafiaGameReadyBody,
) : SessionMessage {
    override val action = ResponseAction.GAME_READY
    override val status = SessionMessage.Status.OK
}

data class MafiaGameReadyBody(
    val turn: Int,
    val player: MafiaPlayerResponse,
)
