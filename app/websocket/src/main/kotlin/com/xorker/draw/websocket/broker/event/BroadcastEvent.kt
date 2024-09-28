package com.xorker.draw.websocket.broker.event

import com.xorker.draw.room.RoomId
import com.xorker.draw.websocket.message.response.SessionMessage

data class BroadcastEvent(
    val roomId: RoomId,
    val message: SessionMessage,
)
