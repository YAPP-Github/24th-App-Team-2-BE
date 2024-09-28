package com.xorker.draw.websocket.event

import com.xorker.draw.event.mafia.MafiaGameInfoStatusChangedListener
import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaGameMessenger
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.message.response.MafiaPhaseMessenger
import org.springframework.stereotype.Component

@Component
internal class MafiaGameInfoWebSocketListener(
    private val mafiaPhaseMessenger: MafiaPhaseMessenger,
    private val mafiaGameMessenger: MafiaGameMessenger,
) : MafiaGameInfoStatusChangedListener {

    override fun connectUser(gameInfo: MafiaGameInfo, userId: UserId) {
        mafiaPhaseMessenger.unicastPhase(userId, gameInfo)
        mafiaGameMessenger.broadcastPlayerList(gameInfo)
    }

    override fun disconnectUser(gameInfo: MafiaGameInfo) {
        mafiaGameMessenger.broadcastPlayerList(gameInfo)
    }

    override fun changePhase(gameInfo: MafiaGameInfo) {
        mafiaPhaseMessenger.broadcastPhase(gameInfo)

        if (gameInfo.phase == MafiaPhase.Wait) {
            mafiaGameMessenger.broadcastPlayerList(gameInfo)
        }
    }
}
