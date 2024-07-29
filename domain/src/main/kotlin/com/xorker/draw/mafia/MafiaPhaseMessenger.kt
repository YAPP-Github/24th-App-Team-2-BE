package com.xorker.draw.mafia

import com.xorker.draw.user.UserId

interface MafiaPhaseMessenger {
    fun unicastPhase(userId: UserId, gameInfo: MafiaGameInfo)
    fun broadcastPhase(gameInfo: MafiaGameInfo)
}
