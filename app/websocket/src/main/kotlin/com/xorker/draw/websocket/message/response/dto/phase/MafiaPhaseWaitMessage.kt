package com.xorker.draw.websocket.message.response.dto.phase

import com.xorker.draw.room.RoomId
import com.xorker.draw.websocket.ResponseAction
import com.xorker.draw.websocket.SessionMessage
import com.xorker.draw.websocket.message.response.dto.game.MafiaPlayerResponse

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
