package com.xorker.draw.websocket.message.response.dto.game

import com.xorker.draw.websocket.ResponseAction
import com.xorker.draw.websocket.SessionMessage

data class MafiaGameReactionMessage(
    override val body: MafiaGameReactionBody,
) : SessionMessage {
    override val action = ResponseAction.REACTION
    override val status = SessionMessage.Status.OK
}

data class MafiaGameReactionBody(
    val reaction: String,
)
