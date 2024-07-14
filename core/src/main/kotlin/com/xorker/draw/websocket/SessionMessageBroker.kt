package com.xorker.draw.websocket

interface SessionMessageBroker {
    fun broadcast(event: BroadcastEvent)
    fun broadcast(event: BranchedBroadcastEvent)
}
