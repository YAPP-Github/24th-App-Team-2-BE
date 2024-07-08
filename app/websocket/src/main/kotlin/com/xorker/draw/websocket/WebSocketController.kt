package com.xorker.draw.websocket

import com.xorker.draw.room.RoomUseCase
import com.xorker.draw.user.User
import com.xorker.draw.websocket.dto.PlayerResponse
import com.xorker.draw.websocket.dto.SessionInitializeRequest
import com.xorker.draw.websocket.dto.SessionInitializeResponse
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class WebSocketController(
    private val sessionFactory: SessionFactory,
    private val sessionUseCase: SessionUseCase,
    private val roomUseCase: RoomUseCase,
    private val messageBroker: SessionMessageBroker,
) {

    fun initializeSession(session: WebSocketSession, request: SessionInitializeRequest) {
        val sessionDto = sessionFactory.create(session, request)
        sessionUseCase.registerSession(sessionDto)

        val roomId = sessionDto.roomId

        val room = roomUseCase.getRoom(roomId) ?: return

        val response = SessionInitializeResponse(
            roomId,
            room.sessions.map { it.user.toResponse() }.toList(),
        )

        messageBroker.broadcast(sessionDto.roomId, SessionMessage(Action.WAIT_ROOM_REFRESH, response))
    }

    private fun User.toResponse(): PlayerResponse = PlayerResponse(
        id = this.id,
        nickname = this.name,
    )
}
