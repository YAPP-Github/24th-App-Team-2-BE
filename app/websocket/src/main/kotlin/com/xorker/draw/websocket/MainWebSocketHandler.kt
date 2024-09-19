package com.xorker.draw.websocket

import com.xorker.draw.exception.InvalidWebSocketStatusException
import com.xorker.draw.exception.XorkerException
import com.xorker.draw.support.logging.logger
import com.xorker.draw.support.metric.MetricManager
import com.xorker.draw.websocket.exception.WebSocketExceptionHandler
import com.xorker.draw.websocket.log.WebSocketLogger
import com.xorker.draw.websocket.message.request.WebSocketRequestParser
import com.xorker.draw.websocket.session.SessionId
import com.xorker.draw.websocket.session.SessionManager
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
internal class MainWebSocketHandler(
    private val sessionManager: SessionManager,
    private val router: WebSocketRouter,
    private val requestParser: WebSocketRequestParser,
    private val waitingQueueUseCase: WaitingQueueUseCase,
    private val sessionEventListener: List<SessionEventListener>,
    private val webSocketExceptionHandler: WebSocketExceptionHandler,
    private val metricManager: MetricManager,
    private val webSocketLogger: WebSocketLogger,
) : TextWebSocketHandler() {

    private val logger = logger()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        metricManager.increaseWebsocket(session.id)
        webSocketLogger.afterConnectionEstablished(session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val request = requestParser.parse(message.payload)

        webSocketLogger.handleRequest(session, request) { s, req ->
            try {
                router.route(s, req)
            } catch (ex: XorkerException) {
                webSocketExceptionHandler.handleXorkerException(s, req.action, ex)
            }
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        metricManager.decreaseWebsocket(session.id)
        webSocketLogger.afterConnectionClosed(session, status)

        val sessionId = SessionId(session.id)

        val sessionDto = sessionManager.unregisterSession(sessionId)
            ?: return logger.error(InvalidWebSocketStatusException.message, InvalidWebSocketStatusException)

        waitingQueueUseCase.remove(sessionDto.user, sessionDto.locale)

        when (status) {
            CloseStatus.NORMAL ->
                sessionEventListener.forEach {
                    it.exitSession(sessionDto.user.id)
                }

            else ->
                sessionEventListener.forEach {
                    it.disconnectSession(sessionDto.user.id)
                }
        }
    }
}
