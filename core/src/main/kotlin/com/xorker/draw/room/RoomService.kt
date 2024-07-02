package com.xorker.draw.room

import com.xorker.draw.user.User
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.SessionInfo
import org.springframework.stereotype.Service

@Service
class RoomService(
    private val roomRepository: RoomRepository,
) {
    fun createRoom(sessionId: String, userId: UserId) {
        join(generateRoomId(), sessionId, userId)
    }

    fun join(roomId: RoomId, sessionId: String, userId: UserId) {
        val user = User(userId, "name")
        roomRepository.joinUser(SessionInfo(roomId, sessionId, user))
        notifyRoomInfo(roomId)
    }

    fun exit(sessionId: String) {
        val roomId = roomRepository.exitUser(sessionId)
        notifyRoomInfo(roomId)
    }

    private fun notifyRoomInfo(roomId: RoomId) {
    }

    private fun generateRoomId(): RoomId {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val value = (1..6)
            .map { charset.random() }
            .joinToString("")

        return RoomId(value)
    }
}
