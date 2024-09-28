package com.xorker.draw.event.mafia

import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.user.UserId

interface MafiaGameInfoStatusChangedListener {
    fun connectUser(gameInfo: MafiaGameInfo, userId: UserId)
    fun disconnectUser(gameInfo: MafiaGameInfo)
    fun changePhase(gameInfo: MafiaGameInfo)
    fun joinRandomMatch(userId: UserId)
}
