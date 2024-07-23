package com.xorker.draw.mafia

sealed class MafiaPhase {
    data object Wait : MafiaPhase()

    class Playing(
        var turn: Int = 0,
        var round: Int = 1,
        val turnList: List<MafiaPlayer>,
        val mafiaPlayer: MafiaPlayer,
        val keyword: MafiaKeyword,
    ) : MafiaPhase()

    class Vote() : MafiaPhase()

    class InferAnswer() : MafiaPhase()

    class End() : MafiaPhase()
}
