package com.xorker.draw.test

import com.xorker.draw.room.RoomId
import com.xorker.draw.websocket.Action
import com.xorker.draw.websocket.SessionMessage
import com.xorker.draw.websocket.broadcaster.SimpleSessionMessageBroker
import org.springframework.stereotype.Component

@Component
class TestService(
    private val broadcaster: SimpleSessionMessageBroker,
) {

    fun test(request: TestRequest) {
        broadcaster.broadcast(RoomId(""), SessionMessage(Action.TEST, request))
    }
}
