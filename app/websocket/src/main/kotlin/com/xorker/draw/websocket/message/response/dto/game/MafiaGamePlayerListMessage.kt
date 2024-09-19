package com.xorker.draw.websocket.message.response.dto.game

import com.xorker.draw.mafia.MafiaPlayer
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.message.response.ResponseAction
import com.xorker.draw.websocket.message.response.SessionMessage

data class MafiaGamePlayerListMessage(
    override val body: MafiaGamePlayerListBody,
) : SessionMessage {
    override val action: ResponseAction = ResponseAction.PLAYER_LIST
    override val status: SessionMessage.Status = SessionMessage.Status.OK
}

data class MafiaGamePlayerListBody(
    val players: List<MafiaPlayerResponse>,
)

data class MafiaPlayerResponse(
    val id: UserId,
    val nickname: String,
    val colorCode: String,
    val isConnect: Boolean,
    val isOwner: Boolean,
)

fun MafiaPlayer.toResponse(owner: MafiaPlayer): MafiaPlayerResponse =
    MafiaPlayerResponse(this.userId, this.nickname, this.color, this.isConnect(), this == owner)
