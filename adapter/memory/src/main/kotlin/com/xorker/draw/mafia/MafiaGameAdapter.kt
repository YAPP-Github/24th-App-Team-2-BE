package com.xorker.draw.mafia

import com.xorker.draw.room.Room
import com.xorker.draw.room.RoomId
import com.xorker.draw.room.RoomRepository
import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Component

@Component
internal class MafiaGameAdapter : MafiaGameRepository, RoomRepository {
    private val data = ConcurrentHashMap<RoomId, MafiaGameInfo>()

    override fun saveGameInfo(gameInfo: MafiaGameInfo) {
        val room = gameInfo.room
        if (room.isEmpty()) {
            data.remove(room.id)
        } else {
            data[room.id] = gameInfo
        }
    }

    override fun getGameInfo(roomId: RoomId): MafiaGameInfo? {
        return data[roomId]
    }

    override fun getRoom(roomId: RoomId): Room<MafiaPlayer>? {
        return data[roomId]?.room
    }
}
