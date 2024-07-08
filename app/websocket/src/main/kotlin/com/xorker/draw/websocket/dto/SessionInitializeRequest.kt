package com.xorker.draw.websocket.dto

import com.xorker.draw.room.RoomId

data class SessionInitializeRequest(
    val accessToken: String,
    val roomId: RoomId?,
)
