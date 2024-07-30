package com.xorker.draw.mafia.phase

import com.xorker.draw.exception.InvalidMafiaPhaseException
import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaPhase
import org.springframework.stereotype.Component

@Component
internal class MafiaPhaseEndGameProcessor {

    // TODO 게임 결과 DB 저장
    internal fun endGame(gameInfo: MafiaGameInfo): MafiaPhase.End {
        val phase = gameInfo.phase

        val endPhase = assertAndGetEndPhase(phase)

        judgeGameResult(endPhase)

        gameInfo.phase = endPhase

        return endPhase
    }

    private fun assertAndGetEndPhase(phase: MafiaPhase): MafiaPhase.End {
        return when (phase) {
            is MafiaPhase.Vote -> {
                phase.toEnd()
            }

            is MafiaPhase.InferAnswer -> {
                phase.toEnd()
            }

            else -> {
                throw InvalidMafiaPhaseException("유효하지 않는 Phase 입니다. 기대값: ${MafiaPhase.Vote::class}, ${MafiaPhase.InferAnswer::class}, 요청값: $phase")
            }
        }
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
