package com.xorker.draw.websocket

import com.xorker.draw.mafia.MafiaStartGameUseCase
import com.xorker.draw.room.RoomId
import com.xorker.draw.websocket.message.request.dto.SessionInitializeRequest
import com.xorker.draw.websocket.message.request.dto.StartMafiaGameRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class WebSocketController(
    private val sessionFactory: SessionFactory,
    private val sessionEventListener: List<SessionEventListener>,
    private val mafiaStartGameUseCase: MafiaStartGameUseCase,
) {

    fun initializeSession(session: WebSocketSession, request: SessionInitializeRequest) {
        val sessionDto = sessionFactory.create(session, request)

        sessionEventListener.forEach {
            it.connectSession(sessionDto, request.nickname)
        }
    }

    fun startMafiaGame(request: StartMafiaGameRequest) {
        mafiaStartGameUseCase.startMafiaGame(RoomId(request.roomId))
    }
}
