package com.xorker.draw.websocket.message.response.dto

import com.xorker.draw.mafia.MafiaPlayer
import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.ResponseAction
import com.xorker.draw.websocket.SessionMessage

data class MafiaPlayerListMessage(
    override val body: MafiaPlayerListBody,
) : SessionMessage {
    override val action: ResponseAction = ResponseAction.PLAYER_LIST
    override val status: SessionMessage.Status = SessionMessage.Status.OK
}

data class MafiaPlayerListBody(
    val roomId: RoomId,
    val players: List<MafiaPlayerResponse>,
)

fun MafiaPlayer.toResponse(owner: MafiaPlayer): MafiaPlayerResponse =
    MafiaPlayerResponse(this.userId, this.nickname, this.color, this.isConnect(), this == owner)

data class MafiaPlayerResponse(
    val id: UserId,
    val nickname: String,
    val colorCode: String,
    val isConnect: Boolean,
    val isOwner: Boolean,
)
