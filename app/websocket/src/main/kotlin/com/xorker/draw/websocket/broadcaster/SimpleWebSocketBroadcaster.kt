package com.xorker.draw.websocket.broadcaster

import com.xorker.draw.websocket.WebSocketSessionManager
import com.xorker.draw.websocket.dto.WebSocketResponse
import com.xorker.draw.websocket.parser.WebSocketResponseParser
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage

@Component
class SimpleWebSocketBroadcaster(
    private val webSocketSessionManager: WebSocketSessionManager,
    private val parser: WebSocketResponseParser,
) : WebSocketBroadcaster {

    override fun broadcast(roomId: String, response: WebSocketResponse) {
        val sessions = webSocketSessionManager.getSessions(roomId) ?: return // TODO: Null Handling
        val message = TextMessage(parser.parse(response))

        sessions.forEach {
            it.sendMessage(message)
        }
    }
}
