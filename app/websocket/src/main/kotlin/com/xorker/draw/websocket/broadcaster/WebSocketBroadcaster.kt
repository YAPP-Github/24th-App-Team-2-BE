package com.xorker.draw.websocket.broadcaster

import com.xorker.draw.websocket.BroadcastEvent

interface WebSocketBroadcaster {
    fun broadcast(event: BroadcastEvent)
}
