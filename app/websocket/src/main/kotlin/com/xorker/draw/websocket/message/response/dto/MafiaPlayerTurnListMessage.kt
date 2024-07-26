package com.xorker.draw.websocket.message.response.dto

import com.xorker.draw.websocket.ResponseAction
import com.xorker.draw.websocket.SessionMessage

class MafiaPlayerTurnListMessage(
    override val body: MafiaPlayerTurnListBody,
) : SessionMessage {
    override val action = ResponseAction.PLAYER_TURN_LIST
    override val status = SessionMessage.Status.OK
}

data class MafiaPlayerTurnListBody(
    val turn: Int,
    val isMyTurn: Boolean = false,
    val players: List<MafiaPlayerResponse>,
)
