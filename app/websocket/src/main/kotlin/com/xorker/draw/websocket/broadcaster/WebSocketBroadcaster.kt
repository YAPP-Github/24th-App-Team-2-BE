package com.xorker.draw.websocket.broadcaster

import com.xorker.draw.room.BroadcastEvent

interface WebSocketBroadcaster {
    fun broadcast(event: BroadcastEvent)
}
