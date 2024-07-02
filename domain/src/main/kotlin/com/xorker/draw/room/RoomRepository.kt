package com.xorker.draw.room

import com.xorker.draw.websocket.SessionInfo

interface RoomRepository {
    fun joinUser(session: SessionInfo)
    fun exitUser(sessionId: String): RoomId
}
