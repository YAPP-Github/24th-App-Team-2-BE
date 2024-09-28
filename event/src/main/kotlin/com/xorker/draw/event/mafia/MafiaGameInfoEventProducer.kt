package com.xorker.draw.event.mafia

import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.user.UserId
import org.springframework.stereotype.Component

@Component
class MafiaGameInfoEventProducer(
    private val listeners: List<MafiaGameInfoStatusChangedListener>,
) {

    fun connectPlayer(gameInfo: MafiaGameInfo, userId: UserId) {
        listeners.forEach {
            it.connectUser(gameInfo, userId)
        }
    }

    fun disconnectUser(gameInfo: MafiaGameInfo) {
        listeners.forEach {
            it.disconnectUser(gameInfo)
        }
    }

    fun changePhase(gameInfo: MafiaGameInfo) {
        listeners.forEach {
            it.changePhase(gameInfo)
        }
    }
}
