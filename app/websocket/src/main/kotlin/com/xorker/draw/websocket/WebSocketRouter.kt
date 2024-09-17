package com.xorker.draw.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.exception.UnSupportedException
import com.xorker.draw.mafia.MafiaGameUseCase
import com.xorker.draw.mafia.phase.MafiaPhaseUseCase
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.message.request.RequestAction
import com.xorker.draw.websocket.message.request.WebSocketRequest
import com.xorker.draw.websocket.message.request.mafia.MafiaGameInferAnswerRequest
import com.xorker.draw.websocket.message.request.mafia.MafiaGameReactionRequest
import com.xorker.draw.websocket.message.request.mafia.MafiaGameVoteMafiaRequest
import com.xorker.draw.websocket.session.SessionId
import com.xorker.draw.websocket.session.SessionManager
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
internal class WebSocketRouter(
    private val objectMapper: ObjectMapper,
    private val webSocketController: WebSocketController,
    private val sessionManager: SessionManager,
    private val mafiaPhaseUseCase: MafiaPhaseUseCase,
    private val mafiaGameUseCase: MafiaGameUseCase,
) {

    fun route(session: WebSocketSession, request: WebSocketRequest) {
        if (request.action == RequestAction.INIT) {
            webSocketController.initializeSession(session, request.extractBody())
            return
        }

        if (request.action == RequestAction.RANDOM_MATCHING) {
            webSocketController.initializeWaitingQueueSession(session, request.extractBody())
            return
        }

        val sessionDto = sessionManager.getSession(SessionId(session.id)) ?: throw InvalidRequestValueException
        // TODO 방법 찾기
//        MDC.put("roomId", sessionDto.roomId.value)

        when (request.action) {
            RequestAction.INIT -> throw UnSupportedException
            RequestAction.RANDOM_MATCHING -> throw UnSupportedException
            RequestAction.START_GAME -> mafiaPhaseUseCase.startGame(sessionDto.user)

            RequestAction.DRAW -> mafiaGameUseCase.draw(sessionDto.user, request.extractBody())
            RequestAction.END_TURN -> mafiaGameUseCase.nextTurnByUser(sessionDto.user)

            RequestAction.VOTE -> {
                val requestDto = request.extractBody<MafiaGameVoteMafiaRequest>()

                mafiaGameUseCase.voteMafia(sessionDto.user, UserId(requestDto.userId))
            }

            RequestAction.ANSWER -> {
                val requestDto = request.extractBody<MafiaGameInferAnswerRequest>()

                mafiaGameUseCase.inferAnswer(sessionDto.user, requestDto.answer)
            }

            RequestAction.DECIDE_ANSWER -> {
                val requestDto = request.extractBody<MafiaGameInferAnswerRequest>()

                mafiaGameUseCase.decideAnswer(sessionDto.user, requestDto.answer)
            }

            RequestAction.REACTION -> {
                val requestDto = request.extractBody<MafiaGameReactionRequest>()

                mafiaGameUseCase.react(sessionDto.user, requestDto.reaction)
            }
        }
    }

    private inline fun <reified T : Any> WebSocketRequest.extractBody(): T {
        return objectMapper.readValue(this.body, T::class.java)
    }
}
