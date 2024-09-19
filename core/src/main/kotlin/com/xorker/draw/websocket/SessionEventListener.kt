package com.xorker.draw.websocket

import com.xorker.draw.room.RoomId
import com.xorker.draw.user.User
import com.xorker.draw.user.UserId

interface SessionEventListener {
    fun connectSession(userId: UserId, roomId: RoomId, nickname: String, locale: String)
    fun connectSession(user: User, roomId: RoomId, locale: String)
    fun disconnectSession(userId: UserId)
    fun exitSession(userId: UserId)
}
