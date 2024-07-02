package com.xorker.draw.websocket.broadcaster

import com.xorker.draw.room.BroadcastEvent
import com.xorker.draw.websocket.WebSocketSessionManager
import com.xorker.draw.websocket.parser.WebSocketResponseParser
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage

@Component
class SimpleWebSocketBroadcaster(
    private val webSocketSessionManager: WebSocketSessionManager,
    private val parser: WebSocketResponseParser,
) : WebSocketBroadcaster {

    @EventListener
    override fun broadcast(event: BroadcastEvent) {
        val sessions = webSocketSessionManager.getSessions(event.roomId) ?: return // TODO: Null Handling
        val message = TextMessage(parser.parse(event))

        sessions.forEach {
            it.sendMessage(message)
        }
    }
}
