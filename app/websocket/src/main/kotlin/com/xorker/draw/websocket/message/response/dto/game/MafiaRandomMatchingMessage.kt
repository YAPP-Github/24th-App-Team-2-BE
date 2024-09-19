package com.xorker.draw.websocket.message.response.dto.game

import com.xorker.draw.websocket.message.response.ResponseAction
import com.xorker.draw.websocket.message.response.SessionMessage

data class MafiaRandomMatchingMessage(
    override val body: MafiaRandomMatchingBody,
) : SessionMessage {
    override val action = ResponseAction.RANDOM_MATCHING
    override val status = SessionMessage.Status.OK
}

class MafiaRandomMatchingBody
