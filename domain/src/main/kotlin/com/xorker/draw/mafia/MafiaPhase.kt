package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidMafiaPhaseException
import com.xorker.draw.mafia.event.JobWithStartTime
import com.xorker.draw.mafia.turn.TurnInfo
import com.xorker.draw.user.UserId
import java.util.Vector
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
    ) : MafiaPhase(), MafiaPhaseWithTurnList, TurnInfo {
        override val round: Int
            get() = turnInfo.round

        override val turn: Int
            get() = turnInfo.turn

        fun toVote(job: JobWithStartTime): Vote {
            val players = mutableMapOf<UserId, Vector<UserId>>()
            turnList.forEach { player ->
                players[player.userId] = Vector()
            }
            return Vote(
                job = job,
                mafiaPlayer = mafiaPlayer,
                keyword = keyword,
                drawData = drawData,
                players = players,
                turnList = turnList,
            )
        }

        fun getDraw(): List<Map<String, Any>> {
            return if (drawData.isEmpty()) {
                emptyList()
            } else {
                drawData.take(drawData.size - 1).map { it.second }
            }
        }

        fun getCurrentDraw(): Map<String, Any> {
            return if (drawData.isEmpty()) {
                emptyMap()
            } else {
                drawData.last().second
            }
        }
    }

    data class Vote(
        val job: JobWithStartTime,
        val mafiaPlayer: MafiaPlayer,
        val keyword: MafiaKeyword,
        val drawData: MutableList<Pair<UserId, Map<String, Any>>>,
        val players: Map<UserId, Vector<UserId>>,
        override val turnList: List<MafiaPlayer>,
    ) : MafiaPhase(), MafiaPhaseWithTurnList {
        fun toInferAnswer(job: JobWithStartTime): InferAnswer {
            return InferAnswer(
                job = job,
                mafiaPlayer = mafiaPlayer,
                keyword = keyword,
                drawData = drawData,
                turnList = turnList,
            )
        }

        fun toEnd(job: JobWithStartTime): End {
            return End(
                job = job,
                mafiaPlayer = mafiaPlayer,
                keyword = keyword,
                drawData = drawData,
                showAnswer = false,
                turnList = turnList,
            )
        }
    }

    class InferAnswer(
        val job: JobWithStartTime,
        val mafiaPlayer: MafiaPlayer,
        val keyword: MafiaKeyword,
        val drawData: MutableList<Pair<UserId, Map<String, Any>>>,
        var answer: String? = null,
        override val turnList: List<MafiaPlayer>,
    ) : MafiaPhase(), MafiaPhaseWithTurnList {
        fun toEnd(job: JobWithStartTime): End {
            return End(
                job = job,
                mafiaPlayer = mafiaPlayer,
                keyword = keyword,
                drawData = drawData,
                answer = answer,
                turnList = turnList,
            )
        }
    }

    class End(
        val job: JobWithStartTime,
        val mafiaPlayer: MafiaPlayer,
        val keyword: MafiaKeyword,
        val drawData: MutableList<Pair<UserId, Map<String, Any>>>,
        val showAnswer: Boolean = true,
        var answer: String? = null,
        var isMafiaWin: Boolean = false,
        override val turnList: List<MafiaPlayer>,
    ) : MafiaPhase(), MafiaPhaseWithTurnList
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

    if (phase !is T) {
        throw InvalidMafiaPhaseException("유효하지 않는 Phase 입니다. 기대값: ${T::class}, 요청값: $phase")
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <reified T1 : MafiaPhase, reified T2 : MafiaPhase> assert(phase: MafiaPhase) {
    contract {
        returns() implies (phase is T1 || phase is T2)
    }

    if (phase !is T1 && phase !is T2) {
        throw InvalidMafiaPhaseException("유효하지 않는 Phase 입니다. 기대값: ${T1::class} or ${T2::class}, 요청값: $phase")
    }
}
