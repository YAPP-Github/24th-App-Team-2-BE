package com.xorker.draw.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.xorker.draw.websocket.message.request.RequestAction
import com.xorker.draw.websocket.message.request.dto.WebSocketRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class WebSocketRouter(
    private val objectMapper: ObjectMapper,
    private val webSocketController: WebSocketController,
) {
    fun route(session: WebSocketSession, request: WebSocketRequest) {
        when (request.action) {
            RequestAction.INIT -> webSocketController.initializeSession(session, request.extractBody())
            RequestAction.START_GAME -> webSocketController.startMafiaGame(request.extractBody())
        }
    }

    private inline fun <reified T : Any> WebSocketRequest.extractBody(): T {
        return objectMapper.readValue(this.body, T::class.java)
    }
}
