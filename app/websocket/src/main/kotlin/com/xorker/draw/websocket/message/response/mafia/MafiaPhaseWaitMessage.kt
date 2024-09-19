package com.xorker.draw.websocket.message.response.mafia

import com.xorker.draw.room.RoomId
import com.xorker.draw.websocket.message.response.ResponseAction
import com.xorker.draw.websocket.message.response.SessionMessage

data class MafiaPhaseWaitMessage(
    override val body: MafiaPhaseWaitBody,
) : SessionMessage {
    override val action: ResponseAction = ResponseAction.PHASE_WAIT
    override val status: SessionMessage.Status = SessionMessage.Status.OK
}

data class MafiaPhaseWaitBody(
    val roomId: RoomId,
    val players: List<MafiaPlayerResponse>,
    val gameOption: MafiaGameOptionResponse,
)
