package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidMafiaPhaseException
import com.xorker.draw.mafia.event.JobWithStartTime
import com.xorker.draw.mafia.turn.TurnInfo
import com.xorker.draw.user.UserId
import java.util.concurrent.ConcurrentHashMap
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed class MafiaPhase {

    data object Wait : MafiaPhase()

    data class Ready(
        val job: JobWithStartTime,
        override val turnList: List<MafiaPlayer>,
        val mafiaPlayer: MafiaPlayer,
        val keyword: MafiaKeyword,
    ) : MafiaPhase(), MafiaPhaseWithTurnList {
        fun toPlaying(job: JobWithStartTime): Playing {
            return Playing(
                turnList = turnList,
                mafiaPlayer = mafiaPlayer,
                keyword = keyword,
                drawData = mutableListOf(),
                timerJob = job,
            )
        }
    }

    class Playing(
        override val turnList: List<MafiaPlayer>,
        val mafiaPlayer: MafiaPlayer,
        val keyword: MafiaKeyword,
        var turnInfo: TurnInfo = TurnInfo(),
        val drawData: MutableList<Pair<UserId, Map<String, Any>>>,
        var timerJob: JobWithStartTime,
    ) : MafiaPhase(), MafiaPhaseWithTurnList, TurnInfo by turnInfo {
        fun toVote(job: JobWithStartTime): Vote {
            val players = ConcurrentHashMap<UserId, MutableList<UserId>>()
            turnList.forEach { player ->
                players[player.userId] = mutableListOf()
            }
            return Vote(
                job = job,
                mafiaPlayer = mafiaPlayer,
                keyword = keyword,
                drawData = drawData,
                players = players,
            )
        }
    }

    class Vote(
        var job: JobWithStartTime,
        val mafiaPlayer: MafiaPlayer,
        val keyword: MafiaKeyword,
        val drawData: MutableList<Pair<UserId, Map<String, Any>>>,
        val players: ConcurrentHashMap<UserId, MutableList<UserId>>,
    ) : MafiaPhase()

    class InferAnswer() : MafiaPhase()

    class End() : MafiaPhase()
}

interface MafiaPhaseWithTurnList {
    val turnList: List<MafiaPlayer>

    fun getPlayerTurn(userId: UserId): Int? {
        turnList.forEachIndexed { index, player ->
            if (player.userId == userId) return index
        }
        return null
    }
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
