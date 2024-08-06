package com.xorker.draw.websocket

import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class WebSocketController(
    private val sessionFactory: SessionFactory,
    private val sessionEventListener: List<SessionEventListener>,
) {

    fun initializeSession(session: WebSocketSession, request: SessionInitializeRequest) {
        val sessionDto = sessionFactory.create(session, request)

        sessionEventListener.forEach {
            it.connectSession(sessionDto, request)
        }
    }
}
