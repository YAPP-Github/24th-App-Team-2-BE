package com.xorker.draw.websocket

import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId

interface SessionMessageBroker {
    fun unicast(userId: UserId, message: SessionMessage)
    fun broadcast(roomId: RoomId, message: SessionMessage)
}
