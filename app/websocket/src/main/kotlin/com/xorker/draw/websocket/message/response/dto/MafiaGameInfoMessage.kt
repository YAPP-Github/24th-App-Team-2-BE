package com.xorker.draw.websocket.message.response.dto

import com.xorker.draw.mafia.MafiaGameOption
import com.xorker.draw.websocket.ResponseAction
import com.xorker.draw.websocket.SessionMessage

data class MafiaGameInfoMessage(
    override val body: Any,
) : SessionMessage {
    override val action: ResponseAction = ResponseAction.GAME_INFO
    override val status: SessionMessage.Status = SessionMessage.Status.OK
}

data class MafiaGameInfoBody(
    val isMafia: Boolean = false,
    val category: String,
    val answer: String,
    val turnList: List<MafiaPlayerResponse>,
    val gameOption: MafiaGameOptionResponse,
)

data class MafiaGameOptionResponse(
    var turnTime: Long,
    var numTurn: Int,
)

fun MafiaGameOption.toResponse(): MafiaGameOptionResponse = MafiaGameOptionResponse(
    turnTime = this.turnTime.toSeconds(),
    numTurn = this.numTurn,
)
