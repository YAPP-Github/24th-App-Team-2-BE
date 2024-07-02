package com.xorker.draw.test

import com.xorker.draw.room.Action
import com.xorker.draw.room.BroadcastEvent
import com.xorker.draw.room.RoomId
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
