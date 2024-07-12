package com.xorker.draw.websocket.dto

import com.xorker.draw.exception.UnSupportedException
import com.xorker.draw.mafia.MafiaPlayer
import com.xorker.draw.room.Player
import com.xorker.draw.user.UserId

interface PlayerResponse {
    val id: UserId
    val nickname: String
}

fun Player.toResponse(): PlayerResponse = when (this) {
    is MafiaPlayer -> MafiaPlayerResponse(this.userId, this.nickname, this.color)
    else -> throw UnSupportedException
}

data class MafiaPlayerResponse(
    override val id: UserId,
    override val nickname: String,
    val colorCode: String,
) : PlayerResponse
