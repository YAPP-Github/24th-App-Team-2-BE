package com.xorker.draw.websocket.dto

import com.xorker.draw.room.RoomId

data class SessionInitializeResponse(
    val roomId: RoomId,
    val players: List<PlayerResponse>,
)
