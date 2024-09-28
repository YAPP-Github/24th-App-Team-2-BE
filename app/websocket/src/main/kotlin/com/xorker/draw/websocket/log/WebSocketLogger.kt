package com.xorker.draw.websocket.log

import com.fasterxml.jackson.databind.ObjectMapper
import com.xorker.draw.mafia.MafiaGameUseCase
import com.xorker.draw.support.logging.defaultApiJsonMap
import com.xorker.draw.support.logging.logger
import com.xorker.draw.support.logging.registerRequestId
import com.xorker.draw.websocket.message.request.RequestAction
import com.xorker.draw.websocket.message.request.WebSocketRequest
import com.xorker.draw.websocket.message.request.mafia.SessionInitializeRequest
import com.xorker.draw.websocket.session.SessionId
import com.xorker.draw.websocket.session.SessionManager
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession

@Component
internal class WebSocketLogger(
    private val sessionManager: SessionManager,
    private val objectMapper: ObjectMapper,
    private val gameUseCase: MafiaGameUseCase,
) {
    private val logger = logger()

    fun handshake(session: WebSocketSession) {
        registerRequestId()

        logger.info(
            objectMapper.writeValueAsString(
                defaultApiJsonMap(
                    "action" to "WS_CONNECT",
                    "sessionId" to session.id,
                ),
            ),
        )
    }

    fun message(session: WebSocketSession, request: WebSocketRequest, origin: () -> Unit) {
        registerRequestId()
        setupMdc(SessionId(session.id))

        try {
            origin()
        } finally {
            val log = generateLog(SessionId(session.id), request)
            logger.info(log)
            MDC.clear()
        }
    }

    fun connectionClosed(session: WebSocketSession, status: CloseStatus, origin: () -> Unit) {
        registerRequestId()
        setupMdc(SessionId(session.id))

        try {
            origin()
        } finally {
            val data = defaultApiJsonMap(
                "action" to "WS_CLOSED",
                "status" to status,
                "sessionId" to session.id,
            )

            injectDefaultSessionInfo(SessionId(session.id), data)

            val log = objectMapper.writeValueAsString(data)
            logger.info(log)
            MDC.clear()
        }
    }

    private fun setupMdc(sessionId: SessionId) {
        val session = sessionManager.getSession(sessionId) ?: return
        val gameInfo = gameUseCase.getGameInfoByUserId(session.user.id) ?: return
        MDC.put("roomId", gameInfo.room.id.value)
    }

    private fun generateLog(sessionId: SessionId, request: WebSocketRequest): String {
        val body: Any? = if (request.action == RequestAction.INIT) {
            objectMapper.readValue(request.body, SessionInitializeRequest::class.java).copy(accessToken = "[masked]")
        } else {
            request.body
        }

        val data = defaultApiJsonMap(
            "action" to request.action,
            "requestBody" to body,
            "sessionId" to sessionId.value,
        )

        injectDefaultSessionInfo(sessionId, data)

        return objectMapper.writeValueAsString(data)
    }

    private fun injectDefaultSessionInfo(sessionId: SessionId, data: MutableMap<String, Any?>) {
        val sessionDto = sessionManager.getSession(sessionId)
        if (sessionDto != null) {
            data["userId"] = sessionDto.user.id.value
            data["roomId"] = MDC.get("roomId")
        }
    }
}
