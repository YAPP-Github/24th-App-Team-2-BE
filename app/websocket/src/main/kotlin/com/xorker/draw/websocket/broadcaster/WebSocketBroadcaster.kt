package com.xorker.draw.websocket.broadcaster

import com.xorker.draw.websocket.dto.WebSocketResponse

interface WebSocketBroadcaster {
    fun broadcast(roomId: String, response: WebSocketResponse)
}
