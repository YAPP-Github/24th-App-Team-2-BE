package com.xorker.draw.websocket

import com.xorker.draw.exception.AlreadyPlayingRoomException
import com.xorker.draw.exception.InvalidRequestOtherPlayingException
import com.xorker.draw.exception.MaxRoomException
import com.xorker.draw.exception.NotFoundRoomException
import com.xorker.draw.mafia.MafiaGameUseCase
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.mafia.UserConnectionUseCase
import com.xorker.draw.room.RoomId
import com.xorker.draw.websocket.message.request.mafia.MafiaGameRandomMatchingRequest
import com.xorker.draw.websocket.message.request.mafia.SessionInitializeRequest
import com.xorker.draw.websocket.session.SessionFactory
import com.xorker.draw.websocket.session.SessionManager
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
internal class WebSocketController(
    private val sessionFactory: SessionFactory,
    private val waitingQueueUseCase: WaitingQueueUseCase,
    private val sessionManager: SessionManager,
    private val mafiaGameUseCase: MafiaGameUseCase,
    private val userConnectionUseCase: UserConnectionUseCase,
) {

    fun initializeWaitingQueueSession(session: WebSocketSession, request: MafiaGameRandomMatchingRequest) {
        val sessionDto = sessionFactory.create(session, request)

        sessionManager.registerSession(sessionDto)
        waitingQueueUseCase.enqueue(sessionDto.user, sessionDto.locale)
    }

    fun initializeSession(session: WebSocketSession, request: SessionInitializeRequest) {
        val sessionDto = sessionFactory.create(session, request)

        val joinedRoomId = mafiaGameUseCase.getGameInfoByUserId(sessionDto.user.id)?.room?.id
        if (joinedRoomId != null && request.roomId != joinedRoomId.value) {
            throw InvalidRequestOtherPlayingException
        }
        sessionManager.registerSession(sessionDto)
        MDC.put("roomId", request.roomId)

        if (request.roomId == null) {
            userConnectionUseCase.connectUser(sessionDto.user, null, request.locale)
            return
        }

        val roomId = RoomId(request.roomId.uppercase())
        val gameInfo = mafiaGameUseCase.getGameInfoByRoomId(roomId) ?: throw NotFoundRoomException

        synchronized(gameInfo) {
            if (gameInfo.phase != MafiaPhase.Wait && gameInfo.room.players.any { it.userId == sessionDto.user.id }.not()) {
                throw AlreadyPlayingRoomException
            }

            if (gameInfo.gameOption.maximum <= gameInfo.room.size()) {
                throw MaxRoomException
            }

            userConnectionUseCase.connectUser(sessionDto.user, roomId, request.locale)
        }
    }
}
