package com.xorker.draw.websocket

import com.xorker.draw.exception.InvalidWebSocketStatusException
import com.xorker.draw.exception.XorkerException
import com.xorker.draw.support.logging.logger
import com.xorker.draw.websocket.exception.WebSocketExceptionHandler
import com.xorker.draw.websocket.parser.WebSocketRequestParser
import io.sentry.Sentry
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
internal class MainWebSocketHandler(
    private val sessionUseCase: SessionUseCase,
    private val waitingQueueSessionUseCase: WaitingQueueSessionUseCase,
    private val router: WebSocketRouter,
    private val requestParser: WebSocketRequestParser,
    private val sessionEventListener: List<SessionEventListener>,
    private val webSocketExceptionHandler: WebSocketExceptionHandler,
) : TextWebSocketHandler() {

    private val log = logger()

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
        val waitingQueueSessionDto = waitingQueueSessionUseCase.getSession(SessionId(session.id))

        if (waitingQueueSessionDto != null) {
            waitingQueueSessionUseCase.unregisterSession(SessionId(session.id))
            return
        }

        val sessionDto = sessionUseCase.getSession(SessionId(session.id))

        if (sessionDto == null) {
            log.error(InvalidWebSocketStatusException.message, InvalidWebSocketStatusException)
            Sentry.captureException(InvalidWebSocketStatusException)
            return
        }

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
