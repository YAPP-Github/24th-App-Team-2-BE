package com.xorker.draw.mafia

import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId

interface MafiaGameRepository {
    fun saveGameInfo(gameInfo: MafiaGameInfo)
    fun removeGameInfo(gameInfo: MafiaGameInfo)
    fun getGameInfo(roomId: RoomId?): MafiaGameInfo?
    fun getGameInfo(userId: UserId): MafiaGameInfo?
    fun removePlayer(userId: UserId)
}
