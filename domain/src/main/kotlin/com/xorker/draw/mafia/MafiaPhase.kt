package com.xorker.draw.mafia

import com.xorker.draw.user.UserId
import java.util.concurrent.ConcurrentHashMap

sealed class MafiaPhase {
    data object Wait : MafiaPhase()

    class Playing(
        var turn: Int = 0,
        var round: Int = 1,
        val turnList: List<MafiaPlayer>,
        val mafiaPlayer: MafiaPlayer,
        val keyword: MafiaKeyword,
        val drawData: MutableList<Pair<UserId, Map<String, Any>>>,
    ) : MafiaPhase()

    class Vote(
        val players: ConcurrentHashMap<UserId, MutableSet<UserId>>,
    ) : MafiaPhase()

    class InferAnswer() : MafiaPhase()

    class End() : MafiaPhase()
}
