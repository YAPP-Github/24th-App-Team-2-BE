package com.xorker.draw.websocket.message.response.mafia

import com.xorker.draw.websocket.message.response.ResponseAction
import com.xorker.draw.websocket.message.response.SessionMessage

data class MafiaGameAnswerMessage(
    override val body: MafiaGameAnswerBody,
) : SessionMessage {
    override val action = ResponseAction.ANSWER
    override val status = SessionMessage.Status.OK
}

data class MafiaGameAnswerBody(
    val answer: String,
)
