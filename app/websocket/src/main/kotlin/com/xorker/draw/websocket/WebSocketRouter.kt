package com.xorker.draw.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.exception.UnSupportedException
import com.xorker.draw.mafia.MafiaGameUseCase
import com.xorker.draw.mafia.phase.MafiaPhaseUseCase
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.message.request.RequestAction
import com.xorker.draw.websocket.message.request.dto.WebSocketRequest
import com.xorker.draw.websocket.message.request.dto.game.MafiaGameInferAnswerRequest
import com.xorker.draw.websocket.message.request.dto.game.MafiaGameVoteMafiaRequest
import org.slf4j.MDC
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
        if (request.action == RequestAction.INIT) {
            webSocketController.initializeSession(session, request.extractBody())
            return
        }

        val sessionDto = sessionUseCase.getSession(SessionId(session.id)) ?: throw InvalidRequestValueException
        MDC.put("roomId", sessionDto.roomId.value)

        when (request.action) {
            RequestAction.INIT -> throw UnSupportedException
            RequestAction.START_GAME -> {
                mafiaPhaseUseCase.startGame(sessionDto.roomId)
            }

            RequestAction.DRAW -> mafiaGameUseCase.draw(sessionDto, request.extractBody())
            RequestAction.END_TURN -> mafiaGameUseCase.nextTurnByUser(sessionDto)

            RequestAction.VOTE -> {
                val requestDto = request.extractBody<MafiaGameVoteMafiaRequest>()

                mafiaGameUseCase.voteMafia(sessionDto, UserId(requestDto.userId))
            }

            RequestAction.ANSWER -> {
                val requestDto = request.extractBody<MafiaGameInferAnswerRequest>()

                mafiaGameUseCase.inferAnswer(sessionDto, requestDto.answer)
            }

            RequestAction.DECIDE_ANSWER -> {
                val requestDto = request.extractBody<MafiaGameInferAnswerRequest>()

                mafiaGameUseCase.decideAnswer(sessionDto, requestDto.answer)
            }
        }
    }

    private inline fun <reified T : Any> WebSocketRequest.extractBody(): T {
        return objectMapper.readValue(this.body, T::class.java)
    }
}
