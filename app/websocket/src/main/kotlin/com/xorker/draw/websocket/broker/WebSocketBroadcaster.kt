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

interface WebSocketBroadcaster {
    fun unicast(userId: UserId, message: SessionMessage)
    fun broadcast(roomId: RoomId, sessionMessage: SessionMessage)
    fun publishBroadcastEvent(event: BroadcastEvent)
    fun publishBranchedBroadcastEvent(event: BranchedBroadcastEvent)
    fun publishRespectiveBroadcastEvent(event: RespectiveBroadcastEvent)
}

@Component
internal class WebSocketBroadcasterSingleInstance(
    private val publisher: ApplicationEventPublisher,
) : WebSocketBroadcaster {
    override fun unicast(userId: UserId, message: SessionMessage) {
        publisher.publishEvent(UnicastEvent(userId, message))
    }

    override fun broadcast(roomId: RoomId, sessionMessage: SessionMessage) {
        publisher.publishEvent(BroadcastEvent(roomId, sessionMessage))
    }

    override fun publishBroadcastEvent(event: BroadcastEvent) {
        publisher.publishEvent(event)
    }

    override fun publishBranchedBroadcastEvent(event: BranchedBroadcastEvent) {
        publisher.publishEvent(event)
    }

    override fun publishRespectiveBroadcastEvent(event: RespectiveBroadcastEvent) {
        publisher.publishEvent(event)
    }
}
