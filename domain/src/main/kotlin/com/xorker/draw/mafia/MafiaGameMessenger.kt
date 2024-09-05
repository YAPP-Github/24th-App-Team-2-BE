package com.xorker.draw.mafia

import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId

interface MafiaGameMessenger {
    fun broadcastPlayerList(gameInfo: MafiaGameInfo)
    fun broadcastDraw(roomId: RoomId, data: Map<String, Any>)
    fun broadcastNextTurn(gameInfo: MafiaGameInfo)
    fun broadcastVoteStatus(gameInfo: MafiaGameInfo)
    fun broadcastAnswer(gameInfo: MafiaGameInfo, answer: String)
    fun unicastRandomMatching(userId: UserId)
    fun broadcastReaction(gameInfo: MafiaGameInfo, reaction: MafiaReactionType)
}
