package com.xorker.draw.test

import com.xorker.draw.room.RoomId
import com.xorker.draw.websocket.broadcaster.SimpleWebSocketBroadcaster
import com.xorker.draw.websocket.dto.ResponseAction
import com.xorker.draw.websocket.dto.WebSocketResponse
import org.springframework.stereotype.Component

@Component
class TestService(
    private val broadcaster: SimpleWebSocketBroadcaster,
) {

    fun test(request: TestRequest) {
        broadcaster.broadcast(RoomId(""), WebSocketResponse(ResponseAction.TEST, request))
    }
}
