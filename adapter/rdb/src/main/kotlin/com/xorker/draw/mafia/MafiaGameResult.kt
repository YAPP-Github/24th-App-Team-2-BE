package com.xorker.draw.mafia

import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId

data class MafiaGameResult(
    val roomId: RoomId,
    val language: String,
    val players: List<UserId>,
    val mafia: UserId,
    val category: String,
    val answer: String,
    val mafiaAnswer: String?,
    val isMafiaWin: Boolean,
    val drawData: MutableList<Pair<UserId, Map<String, Any>>>,
)

fun MafiaGameInfo.toMafiaGameResult(): MafiaGameResult {
    val room = this.room

    val phase = this.phase
    assertIs<MafiaPhase.End>(phase)

    return MafiaGameResult(
        roomId = room.id,
        language = room.locale,
        players = room.players.map { it.userId },
        mafia = phase.mafiaPlayer.userId,
        category = phase.keyword.category,
        answer = phase.keyword.answer,
        mafiaAnswer = phase.answer,
        isMafiaWin = phase.isMafiaWin,
        drawData = phase.drawData,
    )
}
