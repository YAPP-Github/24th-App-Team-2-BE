package com.xorker.draw.websocket.broker.event

import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.SessionMessage

data class RespectiveBroadcastEvent(
    val roomId: RoomId,
    val messages: Map<UserId, SessionMessage>,
)
