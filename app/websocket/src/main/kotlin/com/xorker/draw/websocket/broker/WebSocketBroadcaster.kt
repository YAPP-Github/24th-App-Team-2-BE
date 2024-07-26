package com.xorker.draw.websocket.broker

import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.BranchedBroadcastEvent
import com.xorker.draw.websocket.BroadcastEvent
import com.xorker.draw.websocket.RespectiveBroadcastEvent
import com.xorker.draw.websocket.SessionMessage
import com.xorker.draw.websocket.UnicastEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class WebSocketBroadcaster(
    private val publisher: ApplicationEventPublisher,
) {
    fun unicast(userId: UserId, message: SessionMessage) {
        publisher.publishEvent(UnicastEvent(userId, message))
    }

    fun publishBroadcastEvent(roomId: RoomId, sessionMessage: SessionMessage) {
        publisher.publishEvent(BroadcastEvent(roomId, sessionMessage))
    }

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
