package com.xorker.draw.websocket

import com.xorker.draw.room.RoomId

interface SessionMessageBroker {
    fun unicast(sessionId: SessionId, message: SessionMessage)
    fun broadcast(roomId: RoomId, message: SessionMessage)
}
