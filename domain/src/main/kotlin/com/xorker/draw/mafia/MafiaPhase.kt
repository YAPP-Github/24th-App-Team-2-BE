package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidMafiaPhaseException
import com.xorker.draw.user.UserId
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed class MafiaPhase {

    data object Wait : MafiaPhase()

    data class Ready(
        val turnList: List<MafiaPlayer>,
        val mafiaPlayer: MafiaPlayer,
        val keyword: MafiaKeyword,
    ) : MafiaPhase() {
        fun toPlaying(): Playing {
            return Playing(
                turnList = turnList,
                mafiaPlayer = mafiaPlayer,
                keyword = keyword,
                drawData = mutableListOf(),
            )
        }
    }

    class Playing(
        var turn: Int = 0,
        var round: Int = 1,
        val turnList: List<MafiaPlayer>,
        val mafiaPlayer: MafiaPlayer,
        val keyword: MafiaKeyword,
        val drawData: MutableList<Pair<UserId, Map<String, Any>>>,
    ) : MafiaPhase()

    class Vote() : MafiaPhase()

    class InferAnswer() : MafiaPhase()

    class End() : MafiaPhase()
}

@OptIn(ExperimentalContracts::class)
inline fun <reified T : MafiaPhase> assertIs(phase: MafiaPhase) {
    contract {
        returns() implies (phase is T)
    }

    if (phase is T) {
        throw InvalidMafiaPhaseException("유효하지 않는 Phase 입니다. 기대값: ${T::class}, 요청값: $phase")
    }
}
