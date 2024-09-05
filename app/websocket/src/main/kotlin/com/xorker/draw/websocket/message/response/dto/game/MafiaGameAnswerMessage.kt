package com.xorker.draw.websocket.message.response.dto.game

import com.xorker.draw.websocket.ResponseAction
import com.xorker.draw.websocket.SessionMessage

data class MafiaGameAnswerMessage(
    override val body: MafiaGameAnswerBody,
) : SessionMessage {
    override val action = ResponseAction.ANSWER
    override val status = SessionMessage.Status.OK
}

data class MafiaGameAnswerBody(
    val answer: String,
)
