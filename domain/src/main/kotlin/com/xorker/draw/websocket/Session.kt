package com.xorker.draw.websocket

import com.xorker.draw.room.RoomId
import com.xorker.draw.user.User

@JvmInline
value class SessionId(val value: String)

interface Session {
    val id: SessionId
    val user: User
    val roomId: RoomId
    fun send(message: String)
}
