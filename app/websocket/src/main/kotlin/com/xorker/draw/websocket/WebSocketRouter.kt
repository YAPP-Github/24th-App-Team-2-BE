package com.xorker.draw.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.mafia.MafiaGameUseCase
import com.xorker.draw.mafia.MafiaKeywordUseCase
import com.xorker.draw.mafia.MafiaPhaseUseCase
import com.xorker.draw.mafia.MafiaVoteUseCase
import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.message.request.RequestAction
import com.xorker.draw.websocket.message.request.dto.MafiaAnswerRequest
import com.xorker.draw.websocket.message.request.dto.StartMafiaGameRequest
import com.xorker.draw.websocket.message.request.dto.VoteMafiaRequest
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
    private val mafiaVoteUseCase: MafiaVoteUseCase,
    private val mafiaKeywordUseCase: MafiaKeywordUseCase,
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
            RequestAction.END_TURN -> {
                val sessionDto = session.getDto()

                mafiaGameUseCase.nextTurnByUser(sessionDto) {
                    mafiaPhaseUseCase.vote(sessionDto.roomId)
                }
            }

            RequestAction.VOTE -> {
                val requestDto = request.extractBody<VoteMafiaRequest>()
                val userId = UserId(requestDto.userId)
                val sessionDto = sessionUseCase.getSession(SessionId(session.id)) ?: throw InvalidRequestValueException

                mafiaVoteUseCase.voteMafia(sessionDto, userId)
            }

            RequestAction.ANSWER -> {
                val requestDto = request.extractBody<MafiaAnswerRequest>()
                val sessionDto = session.getDto()

                mafiaKeywordUseCase.inferAnswer(sessionDto, requestDto.answer)
            }

            RequestAction.DECIDE_ANSWER -> {
                val requestDto = request.extractBody<MafiaAnswerRequest>()
                val sessionDto = session.getDto()

                mafiaKeywordUseCase.decideAnswer(sessionDto, requestDto.answer) {
                    mafiaPhaseUseCase.endGame(sessionDto.roomId)
                }
            }
        }
    }

    private fun WebSocketSession.getDto(): Session =
        sessionUseCase.getSession(SessionId(this.id)) ?: throw InvalidRequestValueException

    private inline fun <reified T : Any> WebSocketRequest.extractBody(): T {
        return objectMapper.readValue(this.body, T::class.java)
    }
}
