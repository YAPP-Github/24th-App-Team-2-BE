package com.xorker.draw.mafia.phase

import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaGameMessenger
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.mafia.assertIs
import com.xorker.draw.timer.TimerRepository
import org.springframework.stereotype.Component

@Component
internal class MafiaPhasePlayGameProcessor(
    private val timerRepository: TimerRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
) {

    internal fun playMafiaGame(gameInfo: MafiaGameInfo, nextStep: () -> Unit): MafiaPhase.Playing {
        val phase = gameInfo.phase
        assertIs<MafiaPhase.Ready>(phase)

        val gameOption = gameInfo.gameOption

        val job = timerRepository.startTimer(gameOption.turnTime) {
            processNextTurn(gameInfo, nextStep)
        }

        val playingPhase = phase.toPlaying(job)
        gameInfo.phase = playingPhase

        return playingPhase
    }

    internal fun processNextTurn(gameInfo: MafiaGameInfo, nextStep: () -> Unit) {
        val phase = gameInfo.phase
        assertIs<MafiaPhase.Playing>(phase)

        val gameOption = gameInfo.gameOption

        val nextTurn = phase.nextTurn(gameOption.round, gameOption.turnCount)

        if (nextTurn == null) {
            nextStep.invoke()
            return
        }

        phase.turnInfo = nextTurn

        mafiaGameMessenger.broadcastNextTurn(gameInfo)
        phase.timerJob = timerRepository.startTimer(gameInfo.gameOption.turnTime) {
            processNextTurn(gameInfo, nextStep)
        }
    }
}
