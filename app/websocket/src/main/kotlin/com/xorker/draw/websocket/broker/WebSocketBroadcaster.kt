package com.xorker.draw.websocket.broker

import com.xorker.draw.websocket.BroadcastEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class WebSocketBroadcaster(
    private val publisher: ApplicationEventPublisher,
) {
    fun publishBroadcastEvent(event: BroadcastEvent) {
        publisher.publishEvent(event)
    }
}
