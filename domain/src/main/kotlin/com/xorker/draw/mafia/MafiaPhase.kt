package com.xorker.draw.mafia

sealed class MafiaPhase {
    data object Wait : MafiaPhase()

    class Playing(
        val turnList: List<MafiaPlayer>,
        val mafiaPlayer: MafiaPlayer,
        val keyword: MafiaKeyword,
    ) : MafiaPhase()

    class Vote() : MafiaPhase()

    class InferAnswer() : MafiaPhase()

    class End() : MafiaPhase()
}
