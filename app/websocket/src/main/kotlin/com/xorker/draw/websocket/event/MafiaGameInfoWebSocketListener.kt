package com.xorker.draw.websocket.event

import com.xorker.draw.event.mafia.MafiaGameActionListener
import com.xorker.draw.event.mafia.MafiaGameInfoStatusChangedListener
import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.mafia.MafiaReactionType
import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.message.response.MafiaGameMessenger
import com.xorker.draw.websocket.message.response.MafiaPhaseMessenger
import org.springframework.stereotype.Component

@Component
internal class MafiaGameInfoWebSocketListener(
    private val mafiaPhaseMessenger: MafiaPhaseMessenger,
    private val mafiaGameMessenger: MafiaGameMessenger,
) : MafiaGameInfoStatusChangedListener, MafiaGameActionListener {

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

    override fun joinRandomMatch(userId: UserId) {
        mafiaGameMessenger.unicastRandomMatching(userId)
    }

    override fun draw(roomId: RoomId, drawData: Map<String, Any>) {
        mafiaGameMessenger.broadcastDraw(roomId, drawData)
    }

    override fun nextTurn(gameInfo: MafiaGameInfo) {
        mafiaGameMessenger.broadcastNextTurn(gameInfo)
    }

    override fun vote(gameInfo: MafiaGameInfo) {
        mafiaGameMessenger.broadcastVoteStatus(gameInfo)
    }

    override fun answer(gameInfo: MafiaGameInfo, answer: String) {
        mafiaGameMessenger.broadcastAnswer(gameInfo, answer)
    }

    override fun reaction(gameInfo: MafiaGameInfo, reaction: MafiaReactionType) {
        mafiaGameMessenger.broadcastReaction(gameInfo, reaction)
    }
}
