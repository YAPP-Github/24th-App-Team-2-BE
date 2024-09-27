package com.xorker.draw.websocket.broker

import com.xorker.draw.websocket.broker.event.BranchedBroadcastEvent
import com.xorker.draw.websocket.broker.event.BroadcastEvent
import com.xorker.draw.websocket.broker.event.RespectiveBroadcastEvent
import com.xorker.draw.websocket.broker.event.UnicastEvent

interface SessionMessageBroker {
    fun unicast(event: UnicastEvent)
    fun broadcast(event: BroadcastEvent)
    fun broadcast(event: BranchedBroadcastEvent)
    fun broadcast(event: RespectiveBroadcastEvent)
}
