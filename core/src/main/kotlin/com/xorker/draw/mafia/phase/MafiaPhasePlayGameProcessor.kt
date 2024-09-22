package com.xorker.draw.mafia.phase

import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaGameMessenger
import com.xorker.draw.mafia.MafiaGameRepository
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.mafia.assertIs
import com.xorker.draw.timer.TimerRepository
import org.springframework.stereotype.Component

@Component
internal class MafiaPhasePlayGameProcessor(
    private val timerRepository: TimerRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
    private val mafiaGameRepository: MafiaGameRepository,
) {

    internal fun playMafiaGame(gameInfo: MafiaGameInfo, nextStep: () -> Unit): MafiaPhase.Playing {
        val phase = gameInfo.phase
        assertIs<MafiaPhase.Ready>(phase)

        val gameOption = gameInfo.gameOption

        var time = gameOption.turnTime
        time = time.plus(gameOption.introAnimationTime)
        time = time.plus(gameOption.roundAnimationTime)

        val room = gameInfo.room
        timerRepository.startTimer(room.id, time) {
            processNextTurn(gameInfo, nextStep)
        }

        val playingPhase = phase.toPlaying()
        gameInfo.phase = playingPhase

        mafiaGameRepository.saveGameInfo(gameInfo)

        return playingPhase
    }

    internal fun processNextTurn(gameInfo: MafiaGameInfo, nextStep: () -> Unit) {
        val phase = gameInfo.phase
        assertIs<MafiaPhase.Playing>(phase)

        val gameOption = gameInfo.gameOption

        val room = gameInfo.room
        val nextTurn = phase.nextTurn(gameOption.round - 1, room.size() - 1)

        if (nextTurn == null) {
            nextStep.invoke()
            return
        }

        phase.turnInfo = nextTurn

        val time =
            if (nextTurn.isFirstTurn()) {
                gameOption.turnTime.plus(gameOption.roundAnimationTime)
            } else {
                gameOption.turnTime
            }

        timerRepository.startTimer(room.id, time) {
            processNextTurn(gameInfo, nextStep)
        }

        mafiaGameRepository.saveGameInfo(gameInfo)

        mafiaGameMessenger.broadcastNextTurn(gameInfo)
    }
}
