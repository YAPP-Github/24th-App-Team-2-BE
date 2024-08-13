package com.xorker.draw.websocket

import com.xorker.draw.exception.InvalidRequestOtherPlayingException
import com.xorker.draw.mafia.MafiaGameUseCase
import com.xorker.draw.mafia.MafiaPhase
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class WebSocketController(
    private val sessionFactory: SessionFactory,
    private val sessionEventListener: List<SessionEventListener>,
    private val mafiaGameUseCase: MafiaGameUseCase,
) {

    fun initializeSession(session: WebSocketSession, request: SessionInitializeRequest) {
        val sessionDto = sessionFactory.create(session, request)

        val gameInfo = mafiaGameUseCase.getGameInfo(sessionDto.user.id)

        if (gameInfo != null && gameInfo.phase != MafiaPhase.Wait && gameInfo.room.id.value != request.roomId) {
            throw InvalidRequestOtherPlayingException
        }

        sessionEventListener.forEach {
            it.connectSession(sessionDto, request)
        }
    }
}
