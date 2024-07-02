package com.xorker.draw.websocket

import com.xorker.draw.room.RoomId
import com.xorker.draw.user.User

data class SessionInfo(
    val roomId: RoomId,
    val sessionId: String,
    val user: User,
)
