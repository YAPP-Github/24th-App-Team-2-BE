package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.timer.TimerRepository
import com.xorker.draw.websocket.Session
import org.springframework.stereotype.Service

@Service
internal class MafiaKeywordService(
    private val mafiaGameRepository: MafiaGameRepository,
    private val timerRepository: TimerRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
) : MafiaKeywordUseCase {

    override fun inferAnswer(session: Session, answer: String) {
        val gameInfo = session.getGameInfo()

        val phase = gameInfo.phase
        assertIs<MafiaPhase.InferAnswer>(phase)

        phase.answer = answer

        mafiaGameMessenger.broadcastAnswer(gameInfo, answer)
    }

    override fun decideAnswer(session: Session, answer: String, nextStep: () -> Unit) {
        val gameInfo = session.getGameInfo()

        val phase = gameInfo.phase
        assertIs<MafiaPhase.InferAnswer>(phase)

        phase.answer = answer

        val job = phase.job
        job.cancel()

        processInferAnswer(gameInfo, nextStep)
    }

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

    private fun Session.getGameInfo(): MafiaGameInfo =
        mafiaGameRepository.getGameInfo(roomId) ?: throw InvalidRequestValueException
}
