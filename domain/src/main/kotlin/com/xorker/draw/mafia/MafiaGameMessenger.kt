package com.xorker.draw.mafia

import com.xorker.draw.room.Room
import com.xorker.draw.room.RoomId

interface MafiaGameMessenger {
    fun broadcastPlayerList(room: Room<MafiaPlayer>)
    fun broadcastPlayerTurnList(mafiaGameInfo: MafiaGameInfo)
    fun broadcastDraw(roomId: RoomId, phase: MafiaPhase.Playing)
}
