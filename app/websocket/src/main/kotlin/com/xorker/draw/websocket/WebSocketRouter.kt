package com.xorker.draw.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.mafia.MafiaGameUseCase
import com.xorker.draw.mafia.phase.MafiaPhaseUseCase
import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.message.request.RequestAction
import com.xorker.draw.websocket.message.request.dto.WebSocketRequest
import com.xorker.draw.websocket.message.request.dto.game.MafiaGameInferAnswerRequest
import com.xorker.draw.websocket.message.request.dto.game.MafiaGameStartGameRequest
import com.xorker.draw.websocket.message.request.dto.game.MafiaGameVoteMafiaRequest
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
                val requestDto = request.extractBody<MafiaGameStartGameRequest>()

                mafiaPhaseUseCase.startGame(RoomId(requestDto.roomId))
            }

            RequestAction.DRAW -> mafiaGameUseCase.draw(session.getDto(), request.extractBody())
            RequestAction.END_TURN -> mafiaGameUseCase.nextTurnByUser(session.getDto())

            RequestAction.VOTE -> {
                val requestDto = request.extractBody<MafiaGameVoteMafiaRequest>()
                val sessionDto = session.getDto()

                mafiaGameUseCase.voteMafia(sessionDto, UserId(requestDto.userId))
            }

            RequestAction.ANSWER -> {
                val requestDto = request.extractBody<MafiaGameInferAnswerRequest>()
                val sessionDto = session.getDto()

                mafiaGameUseCase.inferAnswer(sessionDto, requestDto.answer)
            }

            RequestAction.DECIDE_ANSWER -> {
                val requestDto = request.extractBody<MafiaGameInferAnswerRequest>()
                val sessionDto = session.getDto()

                mafiaGameUseCase.decideAnswer(sessionDto, requestDto.answer)
            }
        }
    }

    private fun WebSocketSession.getDto(): Session =
        sessionUseCase.getSession(SessionId(this.id)) ?: throw InvalidRequestValueException

    private inline fun <reified T : Any> WebSocketRequest.extractBody(): T {
        return objectMapper.readValue(this.body, T::class.java)
    }
}
