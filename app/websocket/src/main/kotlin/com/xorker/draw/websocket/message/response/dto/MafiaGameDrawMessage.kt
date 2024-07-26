package com.xorker.draw.websocket.message.response.dto

import com.xorker.draw.websocket.ResponseAction
import com.xorker.draw.websocket.SessionMessage
import java.time.LocalDateTime

data class MafiaGameDrawMessage(
    override val body: MafiaGameDrawBody,
) : SessionMessage {
    override val action: ResponseAction = ResponseAction.DRAW
    override val status: SessionMessage.Status = SessionMessage.Status.OK

    constructor(draw: Map<String, Any>) : this(MafiaGameDrawBody(draw))
}

class MafiaGameDrawBody(
    val draw: Map<String, Any>,
)
