package com.xorker.draw.room

import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Component

@Component
internal class RoomAdapter : RoomRepository {
    val roomMap = ConcurrentHashMap<RoomId, Room>()

    override fun saveRoom(room: Room) {
        if (room.isEmpty()) {
            roomMap.remove(room.id)
        } else {
            roomMap[room.id] = room
        }
    }

    override fun getRoom(roomId: RoomId): Room? {
        return roomMap[roomId]?.copy()
    }
}
