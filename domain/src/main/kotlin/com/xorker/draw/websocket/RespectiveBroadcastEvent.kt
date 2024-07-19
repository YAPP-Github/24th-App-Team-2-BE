package com.xorker.draw.websocket

import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId

data class RespectiveBroadcastEvent(
    val roomId: RoomId,
    val messages: Map<UserId, SessionMessage>,
)
