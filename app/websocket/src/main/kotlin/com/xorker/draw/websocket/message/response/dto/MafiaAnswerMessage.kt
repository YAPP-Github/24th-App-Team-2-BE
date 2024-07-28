package com.xorker.draw.websocket.message.response.dto

import com.xorker.draw.websocket.ResponseAction
import com.xorker.draw.websocket.SessionMessage

class MafiaAnswerMessage(
    override val body: Any,
) : SessionMessage {
    override val action = ResponseAction.ANSWER
    override val status = SessionMessage.Status.OK
}

data class MafiaAnswerBody(
    val answer: String,
)
