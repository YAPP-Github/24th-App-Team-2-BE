package com.xorker.draw.websocket.broadcaster

import com.xorker.draw.room.RoomId
import com.xorker.draw.websocket.dto.WebSocketResponse

interface WebSocketBroadcaster {
    fun broadcast(roomId: RoomId, response: WebSocketResponse)
}
