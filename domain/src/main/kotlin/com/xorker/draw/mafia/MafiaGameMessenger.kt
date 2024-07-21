package com.xorker.draw.mafia

import com.xorker.draw.room.Room

interface MafiaGameMessenger {
    fun broadcastPlayerList(room: Room<MafiaPlayer>)
    fun broadcastGameInfo(mafiaGameInfo: MafiaGameInfo)
    fun broadcastGameReady(mafiaGameInfo: MafiaGameInfo)
    fun broadcastPlayerTurnList(mafiaGameInfo: MafiaGameInfo)
    fun broadcastDraw(mafiaGameInfo: MafiaGameInfo)
}
