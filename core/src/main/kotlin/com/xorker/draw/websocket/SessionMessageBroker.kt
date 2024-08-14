package com.xorker.draw.websocket

interface SessionMessageBroker {
    fun unicast(event: UnicastEvent)
    fun broadcast(event: BroadcastEvent)
    fun broadcast(event: BranchedBroadcastEvent)
    fun broadcast(event: RespectiveBroadcastEvent)
}
