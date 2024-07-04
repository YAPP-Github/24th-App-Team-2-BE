package com.xorker.draw.room

import com.xorker.draw.websocket.Session
import org.springframework.stereotype.Service

@Service
internal class RoomService(
    private val roomPort: RoomPort,
) : RoomUseCase {
    override fun getRoom(roomId: RoomId): Room? {
        return roomPort.getRoom(roomId)
    }

    fun connect(roomId: RoomId, session: Session): Room {
        val room = getRoom(roomId) ?: Room(roomId)
        room.put(session)
        roomPort.saveRoom(room)
        return room
    }

    fun disconnect(roomId: RoomId, session: Session) {
        val room = getRoom(roomId) ?: return
        room.remove(session)
    }
}
