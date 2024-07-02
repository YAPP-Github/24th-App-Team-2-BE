package com.xorker.draw.test

import com.xorker.draw.room.RoomId
import com.xorker.draw.websocket.Action
import com.xorker.draw.websocket.BroadcastEvent
import com.xorker.draw.websocket.broadcaster.SimpleWebSocketBroadcaster
import org.springframework.stereotype.Component

@Component
class TestService(
    private val broadcaster: SimpleWebSocketBroadcaster,
) {

    fun test(request: TestRequest) {
        broadcaster.broadcast(BroadcastEvent(RoomId(""), Action.TEST, request))
    }
}
