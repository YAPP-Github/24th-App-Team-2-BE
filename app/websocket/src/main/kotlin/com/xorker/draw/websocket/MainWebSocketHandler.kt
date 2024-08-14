package com.xorker.draw.websocket

import com.xorker.draw.exception.XorkerException
import com.xorker.draw.websocket.exception.WebSocketExceptionHandler
import com.xorker.draw.websocket.parser.WebSocketRequestParser
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class MainWebSocketHandler(
    private val sessionUseCase: SessionUseCase,
    private val router: WebSocketRouter,
    private val requestParser: WebSocketRequestParser,
    private val sessionEventListener: List<SessionEventListener>,
    private val webSocketExceptionHandler: WebSocketExceptionHandler,
) : TextWebSocketHandler() {
    override fun afterConnectionEstablished(session: WebSocketSession) {
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val request = requestParser.parse(message.payload)

        try {
            router.route(session, request)
        } catch (ex: XorkerException) {
            webSocketExceptionHandler.handleXorkerException(session, request.action, ex)
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val sessionDto = sessionUseCase.getSession(SessionId(session.id)) ?: return // TODO error logging

        when (status) {
            CloseStatus.NORMAL ->
                sessionEventListener.forEach {
                    it.exitSession(sessionDto)
                }

            else ->
                sessionEventListener.forEach {
                    it.disconnectSession(sessionDto)
                }
        }
    }
}
