package com.xorker.draw.websocket

import com.xorker.draw.websocket.parser.WebSocketRequestParser
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class MainWebSocketHandler(
    private val sessionManager: WebSocketSessionManager,
    private val router: WebSocketRouter,
    private val requestParser: WebSocketRequestParser,
) : TextWebSocketHandler() {
    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessionManager.startSession("", session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val request = requestParser.parse(message.payload)

        router.route(request)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessionManager.endSession("", session)
    }
}
