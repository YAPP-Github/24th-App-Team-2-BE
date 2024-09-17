package com.xorker.draw.websocket

import com.xorker.draw.exception.AlreadyPlayingRoomException
import com.xorker.draw.exception.InvalidRequestOtherPlayingException
import com.xorker.draw.exception.MaxRoomException
import com.xorker.draw.exception.NotFoundRoomException
import com.xorker.draw.mafia.MafiaGameUseCase
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.mafia.event.MafiaGameRandomMatchingEvent
import com.xorker.draw.mafia.phase.MafiaPhaseUseCase
import com.xorker.draw.room.RoomId
import com.xorker.draw.websocket.message.request.mafia.MafiaGameRandomMatchingRequest
import com.xorker.draw.websocket.message.request.mafia.SessionInitializeRequest
import com.xorker.draw.websocket.session.SessionFactory
import com.xorker.draw.websocket.session.SessionManager
import com.xorker.draw.websocket.session.SessionWrapper
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
internal class WebSocketController(
    private val sessionFactory: SessionFactory,
    private val waitingQueueSessionEventListener: List<WaitingQueueSessionEventListener>,
    private val sessionEventListener: List<SessionEventListener>,
    private val sessionManager: SessionManager,
    private val mafiaGameUseCase: MafiaGameUseCase,
    private val mafiaPhaseUseCase: MafiaPhaseUseCase,
) {

    fun initializeWaitingQueueSession(session: WebSocketSession, request: MafiaGameRandomMatchingRequest) {
        val waitingQueueSessionDto = sessionFactory.create(session, request)

        waitingQueueSessionEventListener.forEach {
            it.connectSession(waitingQueueSessionDto.user, waitingQueueSessionDto.locale)
        }
    }

    fun initializeSession(session: WebSocketSession, request: SessionInitializeRequest) {
        val sessionDto = sessionFactory.create(session, request)

        val joinedRoomId = mafiaGameUseCase.getGameInfoByUserId(sessionDto.user.id)?.room?.id
        if (joinedRoomId != null && request.roomId != joinedRoomId.value) {
            throw InvalidRequestOtherPlayingException
        }

        if (request.roomId == null) {
            sessionManager.registerSession(sessionDto)
            sessionEventListener.forEach {
                it.connectSession(sessionDto.user.id, sessionDto.roomId, request.nickname, request.locale)
            }
            return
        }

        val gameInfo = mafiaGameUseCase.getGameInfoByRoomId(sessionDto.roomId) ?: throw NotFoundRoomException

        synchronized(gameInfo) {
            if (gameInfo.phase != MafiaPhase.Wait && gameInfo.room.players.any { it.userId == sessionDto.user.id }.not()) {
                throw AlreadyPlayingRoomException
            }

            if (gameInfo.gameOption.maximum <= gameInfo.room.size()) {
                throw MaxRoomException
            }

            sessionManager.registerSession(sessionDto)
            sessionEventListener.forEach {
                it.connectSession(sessionDto.user.id, sessionDto.roomId, request.nickname, request.locale)
            }
        }
    }

    @EventListener
    fun initializeSession(event: MafiaGameRandomMatchingEvent) {
        val players = event.players

        val roomId = RoomId(sessionFactory.generateRoomId())

        players.forEach { user ->
            val sessionDto = SessionWrapper(
                session = this.session, // TODO: fix
                roomId = roomId,
                user = user,
            )

            sessionManager.registerSession(sessionDto)
            sessionEventListener.forEach { eventListener ->
                eventListener.connectSession(sessionDto.user, sessionDto.roomId, event.locale)
            }

            waitingQueueSessionEventListener.forEach { eventListener ->
                eventListener.exitSession(user, event.locale)
            }
        }

        mafiaPhaseUseCase.startGame(roomId)
    }
}
