package com.xorker.draw.mafia

import com.xorker.draw.room.Room
import com.xorker.draw.room.RoomId

interface MafiaGameMessenger {
    fun broadcastPlayerList(room: Room<MafiaPlayer>)
    fun broadcastGameInfo(mafiaGameInfo: MafiaGameInfo)
    fun broadcastGameReady(mafiaGameInfo: MafiaGameInfo)
    fun broadcastPlayerTurnList(mafiaGameInfo: MafiaGameInfo)
    fun broadcastDraw(roomId: RoomId, phase: MafiaPhase.Playing)
    fun broadcastVoteStatus(mafiaGameInfo: MafiaGameInfo)
}
