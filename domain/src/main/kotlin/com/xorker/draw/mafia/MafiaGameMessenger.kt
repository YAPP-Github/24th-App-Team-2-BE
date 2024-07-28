package com.xorker.draw.mafia

import com.xorker.draw.room.RoomId

interface MafiaGameMessenger {
    fun broadcastPlayerList(gameInfo: MafiaGameInfo)
    fun broadcastPlayerTurnList(mafiaGameInfo: MafiaGameInfo)
    fun broadcastDraw(roomId: RoomId, data: Map<String, Any>)
    fun broadcastNextTurn(gameInfo: MafiaGameInfo)
    fun broadcastVoteStatus(mafiaGameInfo: MafiaGameInfo)
    fun broadcastAnswer(gameInfo: MafiaGameInfo, answer: String)
}
