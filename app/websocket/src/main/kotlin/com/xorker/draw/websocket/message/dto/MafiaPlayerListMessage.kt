package com.xorker.draw.websocket.message.dto

import com.xorker.draw.mafia.MafiaPlayer
import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.message.ResponseAction
import com.xorker.draw.websocket.message.SessionMessage

data class MafiaPlayerListMessage(
    override val body: MafiaPlayerListBody,
    override val action: ResponseAction = ResponseAction.PLAYER_LIST,
    override val status: SessionMessage.Status = SessionMessage.Status.OK,
) : SessionMessage

data class MafiaPlayerListBody(
    val roomId: RoomId,
    val players: List<MafiaPlayerResponse>,
)

fun MafiaPlayer.toResponse(): MafiaPlayerResponse =
    MafiaPlayerResponse(this.userId, this.nickname, this.color)

data class MafiaPlayerResponse(
    val id: UserId,
    val nickname: String,
    val colorCode: String,
)
