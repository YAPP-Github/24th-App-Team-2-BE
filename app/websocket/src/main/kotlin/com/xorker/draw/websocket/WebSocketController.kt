package com.xorker.draw.websocket

import com.xorker.draw.room.RoomRepository
import com.xorker.draw.websocket.dto.SessionInitializeRequest
import com.xorker.draw.websocket.dto.SessionInitializeResponse
import com.xorker.draw.websocket.dto.toResponse
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

        val roomId = sessionDto.roomId
        val room = roomRepository.getRoom(roomId) ?: return

        val response = SessionInitializeResponse(
            roomId,
            room.players.map { it.toResponse() }.toList(),
        )

        messageBroker.broadcast(sessionDto.roomId, SessionMessage(Action.WAIT_ROOM_REFRESH, response))
    }
}
