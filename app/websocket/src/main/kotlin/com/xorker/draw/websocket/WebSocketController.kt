package com.xorker.draw.websocket

import com.xorker.draw.exception.AlreadyPlayingRoomException
import com.xorker.draw.exception.InvalidRequestOtherPlayingException
import com.xorker.draw.exception.MaxRoomException
import com.xorker.draw.exception.NotFoundRoomException
import com.xorker.draw.mafia.MafiaGameUseCase
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.room.RoomId
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

        val roomId = request.roomId
        if (roomId != null) {
            val gameInfo = mafiaGameUseCase.getGameInfo(RoomId(roomId)) ?: throw NotFoundRoomException

            if (gameInfo.phase != MafiaPhase.Wait && gameInfo.room.players.any { it.userId == sessionDto.user.id }.not()) {
                throw AlreadyPlayingRoomException
            }

            if (gameInfo.gameOption.maximum <= gameInfo.room.size()) {
                throw MaxRoomException
            }
        }

        val joinedRoomId = mafiaGameUseCase.getGameInfo(sessionDto.user.id)?.room?.id
        if (joinedRoomId != null && roomId != joinedRoomId.value) {
            throw InvalidRequestOtherPlayingException
        }

        sessionEventListener.forEach {
            it.connectSession(sessionDto, request)
        }
    }
}
