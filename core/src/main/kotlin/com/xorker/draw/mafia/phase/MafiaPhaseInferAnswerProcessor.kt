package com.xorker.draw.mafia.phase

import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.mafia.assertIs
import com.xorker.draw.timer.TimerRepository
import org.springframework.stereotype.Component

@Component
internal class MafiaPhaseInferAnswerProcessor(
    private val timerRepository: TimerRepository,
) {

    internal fun playInferAnswer(gameInfo: MafiaGameInfo, nextStep: () -> Unit): MafiaPhase.InferAnswer {
        val phase = gameInfo.phase
        assertIs<MafiaPhase.Vote>(phase)

        val gameOption = gameInfo.gameOption

        val job = timerRepository.startTimer(gameOption.answerTime) {
            processInferAnswer(gameInfo, nextStep)
        }

        val inferAnswerPhase = phase.toInferAnswer(job)
        gameInfo.phase = inferAnswerPhase

        return inferAnswerPhase
    }

    private fun processInferAnswer(gameInfo: MafiaGameInfo, nextStep: () -> Unit) {
        val phase = gameInfo.phase
        assertIs<MafiaPhase.InferAnswer>(phase)

        nextStep.invoke()
    }
}
