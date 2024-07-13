package com.xorker.draw.websocket

import com.xorker.draw.room.RoomId

data class BroadcastEvent(
    val roomId: RoomId,
    val message: SessionMessage,
)
