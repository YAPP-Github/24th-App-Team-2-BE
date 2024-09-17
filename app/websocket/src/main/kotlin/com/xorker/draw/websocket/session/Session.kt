package com.xorker.draw.websocket.session

import com.xorker.draw.room.RoomId
import com.xorker.draw.user.User
import com.xorker.draw.websocket.SessionId

interface Session {
    val id: SessionId
    val user: User
    val roomId: RoomId
    fun send(message: String)
}
