package com.xorker.draw.websocket.broker

import com.xorker.draw.websocket.BranchedBroadcastEvent
import com.xorker.draw.websocket.BroadcastEvent
import com.xorker.draw.websocket.RespectiveBroadcastEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class WebSocketBroadcaster(
    private val publisher: ApplicationEventPublisher,
) {
    fun publishBroadcastEvent(event: BroadcastEvent) {
        publisher.publishEvent(event)
    }

    fun publishBranchedBroadcastEvent(event: BranchedBroadcastEvent) {
        publisher.publishEvent(event)
    }

    fun publishRespectiveBroadcastEvent(event: RespectiveBroadcastEvent) {
        publisher.publishEvent(event)
    }
}
