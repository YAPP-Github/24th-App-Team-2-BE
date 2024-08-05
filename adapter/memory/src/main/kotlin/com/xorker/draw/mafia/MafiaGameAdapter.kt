package com.xorker.draw.mafia

import com.xorker.draw.room.Room
import com.xorker.draw.room.RoomId
import com.xorker.draw.room.RoomRepository
import com.xorker.draw.user.UserId
import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Component

@Component
internal class MafiaGameAdapter : MafiaGameRepository, RoomRepository {
    private val data = ConcurrentHashMap<RoomId, MafiaGameInfo>()
    private val userData = ConcurrentHashMap<UserId, RoomId>()

    override fun saveGameInfo(gameInfo: MafiaGameInfo) {
        val room = gameInfo.room
        if (room.isEmpty()) {
            data.remove(room.id)
        } else {
            data[room.id] = gameInfo
            room.players.forEach {
                userData[it.userId] = room.id
            }
        }
    }

    override fun removeGameInfo(gameInfo: MafiaGameInfo) {
        data.remove(gameInfo.room.id)
    }

    override fun getGameInfo(roomId: RoomId): MafiaGameInfo? {
        return data[roomId]
    }

    override fun getRoom(roomId: RoomId): Room<MafiaPlayer>? {
        return data[roomId]?.room
    }

    override fun getGameInfo(userId: UserId): MafiaGameInfo? {
        val roomId = userData[userId] ?: return null
        return data[roomId]
    }

    override fun removePlayer(userId: UserId) {
        userData.remove(userId)
    }
}
