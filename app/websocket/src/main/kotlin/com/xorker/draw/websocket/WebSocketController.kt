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
import com.xorker.draw.websocket.message.request.WaitingQueueSessionWrapper
import com.xorker.draw.websocket.message.request.dto.game.MafiaGameRandomMatchingRequest
import com.xorker.draw.websocket.message.request.dto.game.SessionInitializeRequest
import com.xorker.draw.websocket.message.request.toSessionWrapper
import com.xorker.draw.websocket.session.SessionFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
internal class WebSocketController(
    private val sessionFactory: SessionFactory,
    private val waitingQueueSessionEventListener: List<WaitingQueueSessionEventListener>,
    private val sessionEventListener: List<SessionEventListener>,
    private val mafiaGameUseCase: MafiaGameUseCase,
    private val mafiaPhaseUseCase: MafiaPhaseUseCase,
) {

    fun initializeWaitingQueueSession(session: WebSocketSession, request: MafiaGameRandomMatchingRequest) {
        val waitingQueueSessionDto = sessionFactory.create(session, request)

        waitingQueueSessionEventListener.forEach {
            it.connectSession(waitingQueueSessionDto)
        }
    }

    fun initializeSession(session: WebSocketSession, request: SessionInitializeRequest) {
        val sessionDto = sessionFactory.create(session, request)
        val roomId = RoomId(request.roomId)

        val joinedRoomId = mafiaGameUseCase.getGameInfo(sessionDto.user.id)?.room?.id
        if (joinedRoomId != null && roomId != joinedRoomId) {
            throw InvalidRequestOtherPlayingException
        }

        if (roomId == null) {
            sessionEventListener.forEach {
                it.connectSession(sessionDto, roomId, request.nickname, request.locale)
            }
            return
        }

        val gameInfo = mafiaGameUseCase.getGameInfo(roomId) ?: throw NotFoundRoomException

        synchronized(gameInfo) {
            if (gameInfo.phase != MafiaPhase.Wait && gameInfo.room.players.any { it.userId == sessionDto.user.id }.not()) {
                throw AlreadyPlayingRoomException
            }

            if (gameInfo.gameOption.maximum <= gameInfo.room.size()) {
                throw MaxRoomException
            }

            sessionEventListener.forEach {
                it.connectSession(sessionDto, roomId, request.nickname, request.locale)
            }
        }
    }

    @EventListener
    fun initializeSession(event: MafiaGameRandomMatchingEvent) {
        val players = event.players

        val roomId = RoomId(sessionFactory.generateRoomId())

        players.forEach {
            if (it is WaitingQueueSessionWrapper) {
                val sessionDto = it.toSessionWrapper(roomId)

                sessionEventListener.forEach { eventListener ->
                    eventListener.connectSession(sessionDto, it.locale)
                }

                waitingQueueSessionEventListener.forEach { eventListener ->
                    eventListener.exitSession(it)
                }
            }
        }

        mafiaPhaseUseCase.startGame(roomId)
    }
}
