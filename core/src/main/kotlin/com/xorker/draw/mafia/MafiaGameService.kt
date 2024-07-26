package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidRequestOnlyMyTurnException
import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.mafia.dto.DrawRequest
import com.xorker.draw.timer.TimerRepository
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.Session
import org.springframework.stereotype.Component
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Component
internal class MafiaGameService(
    private val mafiaGameRepository: MafiaGameRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
    private val timerRepository: TimerRepository,
) : MafiaGameUseCase {

    override fun draw(session: Session, request: DrawRequest) {
        val gameInfo = session.getGameInfo()
        val phase = gameInfo.phase
        assertTurn(phase, session.user.id)

        val drawData = phase.drawData.lastOrNull()
        if (drawData != null && drawData.first == session.user.id) {
            phase.drawData.removeLast()
        }
        phase.drawData.add(Pair(session.user.id, request.drawData))

        mafiaGameRepository.saveGameInfo(gameInfo)
        mafiaGameMessenger.broadcastDraw(gameInfo.room.id, request.drawData)
    }

    override fun nextTurnByUser(session: Session) {
        val gameInfo = session.getGameInfo()
        val phase = gameInfo.phase
        assertTurn(phase, session.user.id)

        phase.timerJob.cancel()

        processNextTurn(gameInfo)
    }

    fun processNextTurn(gameInfo: MafiaGameInfo) {
        val phase = gameInfo.phase
        assertIs<MafiaPhase.Playing>(phase)
        val gameOption = gameInfo.gameOption

        val nextTurn = phase.nextTurn(gameOption.numTurn, gameInfo.room.size())

        if (nextTurn == null) {
            // TODO 투표로 넘기기
            return
        }

        phase.turnInfo = nextTurn

        mafiaGameMessenger.broadcastNextTurn(gameInfo)
        phase.timerJob = timerRepository.startTimer(gameInfo.gameOption.turnTime) {
            processNextTurn(gameInfo)
        }
    }

    private fun Session.getGameInfo(): MafiaGameInfo =
        mafiaGameRepository.getGameInfo(roomId) ?: throw InvalidRequestValueException

    @OptIn(ExperimentalContracts::class)
    private fun assertTurn(phase: MafiaPhase, userId: UserId) {
        contract {
            returns() implies (phase is MafiaPhase.Playing)
        }
        if (phase !is MafiaPhase.Playing) throw InvalidRequestValueException
        if (phase.getPlayerTurn(userId) == phase.turn) throw InvalidRequestOnlyMyTurnException
    }
}
