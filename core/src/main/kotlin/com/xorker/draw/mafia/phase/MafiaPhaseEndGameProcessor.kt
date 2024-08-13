package com.xorker.draw.mafia.phase

import com.xorker.draw.exception.InvalidMafiaPhaseException
import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaGameMessenger
import com.xorker.draw.mafia.MafiaGameRepository
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.mafia.MafiaPhaseMessenger
import com.xorker.draw.mafia.assertIs
import com.xorker.draw.mafia.event.JobWithStartTime
import com.xorker.draw.timer.TimerRepository
import org.springframework.stereotype.Component

@Component
internal class MafiaPhaseEndGameProcessor(
    private val mafiaGameRepository: MafiaGameRepository,
    private val timerRepository: TimerRepository,
    private val mafiaPhaseMessenger: MafiaPhaseMessenger,
    private val mafiaGameMessenger: MafiaGameMessenger,
) {

    // TODO 게임 결과 DB 저장
    internal fun endGame(gameInfo: MafiaGameInfo): MafiaPhase.End {
        val phase = gameInfo.phase

        val gameOption = gameInfo.gameOption

        val job = timerRepository.startTimer(gameOption.endTime) {
            processEndGame(gameInfo)
        }

        val endPhase = assertAndGetEndPhase(job, phase)

        judgeGameResult(endPhase)

        gameInfo.phase = endPhase

        return endPhase
    }

    private fun assertAndGetEndPhase(job: JobWithStartTime, phase: MafiaPhase): MafiaPhase.End {
        return when (phase) {
            is MafiaPhase.Vote -> {
                phase.toEnd(job)
            }

            is MafiaPhase.InferAnswer -> {
                phase.toEnd(job)
            }

            else -> {
                throw InvalidMafiaPhaseException("유효하지 않는 Phase 입니다. 기대값: ${MafiaPhase.Vote::class}, ${MafiaPhase.InferAnswer::class}, 요청값: $phase")
            }
        }
    }

    private fun processEndGame(gameInfo: MafiaGameInfo) {
        val phase = gameInfo.phase
        assertIs<MafiaPhase.End>(phase)

        val room = gameInfo.room

        val players = room.players
        val owner = room.owner

        val joinPlayers = players.filter { it.isConnect() }
            .toList()

        if (joinPlayers.isEmpty()) return

        if (joinPlayers.contains(owner).not()) {
            room.owner = joinPlayers.first()
        }

        room.clear()
        room.addAll(joinPlayers)

        gameInfo.phase = MafiaPhase.Wait

        mafiaGameRepository.saveGameInfo(gameInfo)

        mafiaPhaseMessenger.broadcastPhase(gameInfo)
        mafiaGameMessenger.broadcastPlayerList(gameInfo)
    }

    private fun judgeGameResult(endPhase: MafiaPhase.End) {
        val showAnswer = endPhase.showAnswer
        val keyword = endPhase.keyword
        if (showAnswer.not()) {
            endPhase.isMafiaWin = true
            return
        }
        endPhase.isMafiaWin = keyword.answer == endPhase.answer // TODO 동의어 처리
    }
}
