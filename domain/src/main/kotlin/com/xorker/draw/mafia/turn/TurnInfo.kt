package com.xorker.draw.mafia.turn

interface TurnInfo {
    val round: Int
    val turn: Int

    fun nextTurn(maxRound: Int, maxTurnPerRound: Int): TurnInfo? {
        if (this.round == maxRound && this.turn == maxTurnPerRound) return null

        if (this.turn == maxTurnPerRound) {
            return TurnInfo(round + 1, 0)
        }

        return TurnInfo(round, turn + 1)
    }

    fun isLastTurn(maxTurnPerRound: Int): Boolean = turn == maxTurnPerRound

    fun isLastRound(maxRound: Int): Boolean = round == maxRound
}

fun TurnInfo(round: Int = 0, turn: Int = 0): TurnInfo = TurnInfoImpl(round, turn)

private data class TurnInfoImpl(
    override val round: Int,
    override val turn: Int,
) : TurnInfo
