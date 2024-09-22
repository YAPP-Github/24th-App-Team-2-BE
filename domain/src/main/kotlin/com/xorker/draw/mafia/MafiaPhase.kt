package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidMafiaPhaseException
import com.xorker.draw.mafia.turn.TurnInfo
import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId
import java.util.Vector
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed class MafiaPhase {

    data object Wait : MafiaPhase()

    data class Ready(
        override val jobKey: RoomId,
        override val turnList: List<MafiaPlayer>,
        val mafiaPlayer: MafiaPlayer,
        val keyword: MafiaKeyword,
    ) : MafiaPhase(), MafiaPhaseWithTurnList, MafiaPhaseWithTimer {
        fun toPlaying(): Playing {
            return Playing(
                jobKey = jobKey,
                turnList = turnList,
                mafiaPlayer = mafiaPlayer,
                keyword = keyword,
                drawData = mutableListOf(),
            )
        }
    }

    class Playing(
        override val jobKey: RoomId,
        override val turnList: List<MafiaPlayer>,
        val mafiaPlayer: MafiaPlayer,
        val keyword: MafiaKeyword,
        var turnInfo: TurnInfo = TurnInfo(),
        val drawData: MutableList<Pair<UserId, Map<String, Any>>>,
    ) : MafiaPhase(), MafiaPhaseWithTurnList, TurnInfo, MafiaPhaseWithTimer {
        override val round: Int
            get() = turnInfo.round

        override val turn: Int
            get() = turnInfo.turn

        fun toVote(): Vote {
            val players = mutableMapOf<UserId, Vector<UserId>>()
            turnList.forEach { player ->
                players[player.userId] = Vector()
            }
            return Vote(
                jobKey = jobKey,
                mafiaPlayer = mafiaPlayer,
                keyword = keyword,
                drawData = drawData,
                players = players,
                turnList = turnList,
            )
        }

        fun getDraw(): List<Map<String, Any>> {
            if (drawData.isEmpty()) {
                return emptyList()
            }

            val currentUser = turnList[turnInfo.turn]

            if (drawData.last().first == currentUser.userId) {
                return drawData.take(drawData.size - 1).map { it.second }
            }

            return drawData.map { it.second }
        }

        fun getCurrentDraw(): Map<String, Any> {
            if (drawData.isEmpty()) {
                return emptyMap()
            }

            val currentUser = turnList[turnInfo.turn]

            val last = drawData.last()
            if (last.first == currentUser.userId) {
                return last.second
            }

            return emptyMap()
        }
    }

    data class Vote(
        override val jobKey: RoomId,
        override val turnList: List<MafiaPlayer>,
        val mafiaPlayer: MafiaPlayer,
        val keyword: MafiaKeyword,
        val drawData: MutableList<Pair<UserId, Map<String, Any>>>,
        val players: Map<UserId, Vector<UserId>>,
    ) : MafiaPhase(), MafiaPhaseWithTurnList, MafiaPhaseWithTimer {
        fun toInferAnswer(): InferAnswer {
            return InferAnswer(
                jobKey = jobKey,
                turnList = turnList,
                mafiaPlayer = mafiaPlayer,
                keyword = keyword,
                drawData = drawData,
            )
        }

        fun toEnd(): End {
            return End(
                jobKey = jobKey,
                turnList = turnList,
                mafiaPlayer = mafiaPlayer,
                keyword = keyword,
                drawData = drawData,
                showAnswer = false,
            )
        }
    }

    class InferAnswer(
        override val jobKey: RoomId,
        override val turnList: List<MafiaPlayer>,
        val mafiaPlayer: MafiaPlayer,
        val keyword: MafiaKeyword,
        val drawData: MutableList<Pair<UserId, Map<String, Any>>>,
        var answer: String? = null,
    ) : MafiaPhase(), MafiaPhaseWithTurnList, MafiaPhaseWithTimer {
        fun toEnd(): End {
            return End(
                jobKey = jobKey,
                turnList = turnList,
                mafiaPlayer = mafiaPlayer,
                keyword = keyword,
                drawData = drawData,
                answer = answer,
            )
        }
    }

    class End(
        override val jobKey: RoomId,
        override val turnList: List<MafiaPlayer>,
        val mafiaPlayer: MafiaPlayer,
        val keyword: MafiaKeyword,
        val drawData: MutableList<Pair<UserId, Map<String, Any>>>,
        val showAnswer: Boolean = true,
        var answer: String? = null,
        var isMafiaWin: Boolean = false,
    ) : MafiaPhase(), MafiaPhaseWithTurnList, MafiaPhaseWithTimer
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

interface MafiaPhaseWithTimer {
    val jobKey: RoomId
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
