package com.xorker.draw.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.xorker.draw.test.TestService
import com.xorker.draw.websocket.dto.RequestAction
import com.xorker.draw.websocket.dto.WebSocketRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class WebSocketRouter(
    private val objectMapper: ObjectMapper,
    private val webSocketController: WebSocketController,
    private val testService: TestService,
) {
    fun route(session: WebSocketSession, request: WebSocketRequest) {
        when (request.action) {
            RequestAction.TEST -> testService.test(request.extractBody())
            RequestAction.INIT -> webSocketController.initializeSession(session, request.extractBody())
        }
    }

    private inline fun <reified T : Any> WebSocketRequest.extractBody(): T {
        return objectMapper.readValue(this.body, T::class.java)
    }
}
