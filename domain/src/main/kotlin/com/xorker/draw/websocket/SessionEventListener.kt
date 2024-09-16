package com.xorker.draw.websocket

import com.xorker.draw.room.RoomId

interface SessionEventListener {
    fun connectSession(session: Session, roomId: RoomId?, nickname: String, locale: String)
    fun connectSession(session: Session, locale: String)
    fun disconnectSession(session: Session)
    fun exitSession(session: Session)
}
