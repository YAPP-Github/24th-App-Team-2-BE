package com.xorker.draw.websocket

import com.xorker.draw.room.RoomRepository
import com.xorker.draw.websocket.dto.SessionInitializeRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class WebSocketController(
    private val sessionFactory: SessionFactory,
    private val messageBroker: SessionMessageBroker,
    private val sessionEventListener: List<SessionEventListener>,
    private val roomRepository: RoomRepository,
) {

    fun initializeSession(session: WebSocketSession, request: SessionInitializeRequest) {
        val sessionDto = sessionFactory.create(session, request)

        sessionEventListener.forEach {
            it.connectSession(sessionDto, request.nickname)
        }
    }
}
