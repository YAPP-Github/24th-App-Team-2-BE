package com.xorker.draw.mafia

sealed class MafiaPhase {
    data object Wait : MafiaPhase()

    class Playing(
        val turn: Int = 1,
        val turnList: List<MafiaPlayer>,
        val mafiaPlayer: MafiaPlayer,
        val keyword: MafiaKeyword,
    ) : MafiaPhase()

    class Vote() : MafiaPhase()

    class InferAnswer() : MafiaPhase()

    class End() : MafiaPhase()
}
