package com.xorker.draw.websocket.message.response.mafia

import com.xorker.draw.websocket.message.response.ResponseAction
import com.xorker.draw.websocket.message.response.SessionMessage

data class MafiaGameDrawMessage(
    override val body: MafiaGameDrawBody,
) : SessionMessage {
    override val action: ResponseAction = ResponseAction.DRAW
    override val status: SessionMessage.Status = SessionMessage.Status.OK

    constructor(draw: Map<String, Any>) : this(MafiaGameDrawBody(draw))
}

data class MafiaGameDrawBody(
    val draw: Map<String, Any>,
)
