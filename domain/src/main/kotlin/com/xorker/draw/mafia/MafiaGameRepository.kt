package com.xorker.draw.mafia

import com.xorker.draw.room.RoomId

interface MafiaGameRepository {
    fun saveGameInfo(gameInfo: MafiaGameInfo)
    fun getGameInfo(roomId: RoomId): MafiaGameInfo?
}
