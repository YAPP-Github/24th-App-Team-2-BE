package com.xorker.draw.event.mafia

import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaReactionType
import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId
import org.springframework.stereotype.Component

@Component
class MafiaGameInfoEventProducer(
    private val statusListeners: List<MafiaGameInfoStatusChangedListener>,
    private val actionListeners: List<MafiaGameActionListener>,
) {

    fun connectPlayer(gameInfo: MafiaGameInfo, userId: UserId) {
        statusListeners.forEach {
            it.connectUser(gameInfo, userId)
        }
    }

    fun disconnectUser(gameInfo: MafiaGameInfo) {
        statusListeners.forEach {
            it.disconnectUser(gameInfo)
        }
    }

    fun changePhase(gameInfo: MafiaGameInfo) {
        statusListeners.forEach {
            it.changePhase(gameInfo)
        }
    }

    fun joinRandomMatch(userId: UserId) {
        statusListeners.forEach {
            it.joinRandomMatch(userId)
        }
    }

    fun draw(roomId: RoomId, drawData: Map<String, Any>) {
        actionListeners.forEach {
            it.draw(roomId, drawData)
        }
    }

    fun nextTurn(gameInfo: MafiaGameInfo) {
        actionListeners.forEach {
            it.nextTurn(gameInfo)
        }
    }

    fun vote(gameInfo: MafiaGameInfo) {
        actionListeners.forEach {
            it.vote(gameInfo)
        }
    }

    fun answer(gameInfo: MafiaGameInfo, answer: String) {
        actionListeners.forEach {
            it.answer(gameInfo, answer)
        }
    }

    fun reaction(gameInfo: MafiaGameInfo, reaction: MafiaReactionType) {
        actionListeners.forEach {
            it.reaction(gameInfo, reaction)
        }
    }
}
