package com.xorker.draw.websocket

import com.xorker.draw.websocket.dto.SessionInitializeRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class WebSocketController(
    private val sessionFactory: SessionFactory,
    private val sessionUseCase: SessionUseCase,
) {

    fun initializeSession(session: WebSocketSession, request: SessionInitializeRequest) {
        val sessionDto = sessionFactory.create(session, request)
        sessionUseCase.registerSession(sessionDto)

        // TODO: Broadcast
    }
}
