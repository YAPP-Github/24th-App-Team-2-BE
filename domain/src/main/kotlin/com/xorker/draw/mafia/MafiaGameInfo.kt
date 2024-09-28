package com.xorker.draw.mafia

import com.xorker.draw.room.Room
import com.xorker.draw.user.UserId

data class MafiaGameInfo(
    val room: Room<MafiaPlayer>,
    var phase: MafiaPhase,
    val gameOption: MafiaGameOption,
) {
    fun findPlayer(userId: UserId): MafiaPlayer? = room.findPlayer(userId)
}
