package com.xorker.draw.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.mafia.MafiaGameUseCase
import com.xorker.draw.mafia.MafiaStartGameUseCase
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
    private val mafiaStartGameUseCase: MafiaStartGameUseCase,
    private val mafiaGameUseCase: MafiaGameUseCase,
) {
    fun route(session: WebSocketSession, request: WebSocketRequest) {
        when (request.action) {
            RequestAction.INIT -> webSocketController.initializeSession(session, request.extractBody())
            RequestAction.START_GAME -> {
                val requestDto = request.extractBody<StartMafiaGameRequest>()
                val roomId = RoomId(requestDto.roomId)

                mafiaStartGameUseCase.startMafiaGame(roomId)
            }
            RequestAction.DRAW -> {
                val sessionDto = sessionUseCase.getSession(SessionId(session.id)) ?: throw InvalidRequestValueException
                mafiaGameUseCase.draw(sessionDto, request.extractBody())
            }
        }
    }

    private inline fun <reified T : Any> WebSocketRequest.extractBody(): T {
        return objectMapper.readValue(this.body, T::class.java)
    }
}
