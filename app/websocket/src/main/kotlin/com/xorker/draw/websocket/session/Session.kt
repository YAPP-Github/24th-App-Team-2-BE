package com.xorker.draw.websocket.session

import com.xorker.draw.user.User
import com.xorker.draw.websocket.SessionId

interface Session {
    val id: SessionId
    val user: User
    fun send(message: String)
}
