package com.xorker.draw.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.mafia.MafiaGameUseCase
import com.xorker.draw.mafia.MafiaPhaseUseCase
import com.xorker.draw.room.RoomId
import com.xorker.draw.websocket.message.request.RequestAction
import com.xorker.draw.websocket.message.request.dto.StartMafiaGameRequest
import com.xorker.draw.websocket.message.request.dto.WebSocketRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class WebSocketRouter(
    private val objectMapper: ObjectMapper,
    private val webSocketController: WebSocketController,
    private val sessionUseCase: SessionUseCase,
    private val mafiaPhaseUseCase: MafiaPhaseUseCase,
    private val mafiaGameUseCase: MafiaGameUseCase,
) {
    fun route(session: WebSocketSession, request: WebSocketRequest) {
        when (request.action) {
            RequestAction.INIT -> webSocketController.initializeSession(session, request.extractBody())
            RequestAction.START_GAME -> {
                val requestDto = request.extractBody<StartMafiaGameRequest>()
                val roomId = RoomId(requestDto.roomId)

                mafiaPhaseUseCase.startGame(roomId)
            }

            RequestAction.DRAW -> mafiaGameUseCase.draw(session.getDto(), request.extractBody())
            RequestAction.END_TURN -> mafiaGameUseCase.nextTurn(session.getDto())
        }
    }

    private fun WebSocketSession.getDto(): Session =
        sessionUseCase.getSession(SessionId(this.id)) ?: throw InvalidRequestValueException

    private inline fun <reified T : Any> WebSocketRequest.extractBody(): T {
        return objectMapper.readValue(this.body, T::class.java)
    }
}
