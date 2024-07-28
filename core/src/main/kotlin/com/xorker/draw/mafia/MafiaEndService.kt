package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidMafiaPhaseException
import org.springframework.stereotype.Service

@Service
internal class MafiaEndService {

    internal fun endGame(gameInfo: MafiaGameInfo): MafiaPhase.End {
        val phase = gameInfo.phase

        val endPhase = assertAndGetEndPhase(phase, gameInfo)

        gameInfo.phase = endPhase

        return endPhase
    }

    private fun assertAndGetEndPhase(phase: MafiaPhase, gameInfo: MafiaGameInfo): MafiaPhase.End {
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
}
