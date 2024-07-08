package com.xorker.draw.websocket.dto

import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId

data class SessionInitializeResponse(
    val roomId: RoomId,
    val players: List<PlayerResponse>,
)

data class PlayerResponse(
    val id: UserId,
    val nickname: String,
    val colorCode: String = "ffffff",
)
