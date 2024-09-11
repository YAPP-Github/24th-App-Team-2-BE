package com.xorker.draw.websocket.log

import com.fasterxml.jackson.databind.ObjectMapper
import com.xorker.draw.support.logging.defaultApiJsonMap
import com.xorker.draw.support.logging.logger
import com.xorker.draw.support.logging.registerRequestId
import com.xorker.draw.websocket.SessionId
import com.xorker.draw.websocket.SessionInitializeRequest
import com.xorker.draw.websocket.SessionUseCase
import com.xorker.draw.websocket.message.request.RequestAction
import com.xorker.draw.websocket.message.request.dto.WebSocketRequest
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession

@Component
internal class WebSocketLogger(
    private val sessionUseCase: SessionUseCase,
    private val objectMapper: ObjectMapper,
) {
    private val logger = logger()

    fun afterConnectionEstablished(session: WebSocketSession) {
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

    fun handleRequest(session: WebSocketSession, request: WebSocketRequest, origin: (WebSocketSession, WebSocketRequest) -> Unit) {
        registerRequestId()

        try {
            origin(session, request)
        } finally {
            val log = generateLog(SessionId(session.id), request)
            logger.info(log)
            MDC.clear()
        }
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

    fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        registerRequestId()

        val data = defaultApiJsonMap(
            "action" to "WS_CLOSED",
            "status" to status,
            "sessionId" to session.id,
        )

        injectDefaultSessionInfo(SessionId(session.id), data)

        val log = objectMapper.writeValueAsString(data)
        logger.info(log)
    }

    private fun injectDefaultSessionInfo(sessionId: SessionId, data: MutableMap<String, Any?>) {
        val sessionDto = sessionUseCase.getSession(sessionId)
        if (sessionDto != null) {
            data["userId"] = sessionDto.user.id.value
            data["roomId"] = sessionDto.roomId
        }
    }
}
