package com.xorker.draw.mafia

import com.xorker.draw.room.RoomId

interface MafiaGameMessenger {
    fun broadcastPlayerList(gameInfo: MafiaGameInfo)
    fun broadcastDraw(roomId: RoomId, data: Map<String, Any>)
    fun broadcastNextTurn(gameInfo: MafiaGameInfo)
    fun broadcastVoteStatus(gameInfo: MafiaGameInfo)
    fun broadcastAnswer(gameInfo: MafiaGameInfo, answer: String)
}
