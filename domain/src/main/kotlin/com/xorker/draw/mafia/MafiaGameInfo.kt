package com.xorker.draw.mafia

import com.xorker.draw.room.Room
import com.xorker.draw.user.UserId

class MafiaGameInfo(
    val room: Room<MafiaPlayer>,
    val phase: MafiaPhase,
    val gameOption: MafiaGameOption,
) {
    fun findPlayer(userId: UserId): MafiaPlayer? = room.findPlayer(userId)
}
