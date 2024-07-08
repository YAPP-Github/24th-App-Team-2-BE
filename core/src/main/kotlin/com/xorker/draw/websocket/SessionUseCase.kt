package com.xorker.draw.websocket

import com.xorker.draw.room.RoomId

interface SessionUseCase {
    fun registerSession(session: Session)
    fun unregisterSession(sessionId: SessionId)
    fun getSession(sessionId: SessionId): Session?
    fun getSessionsByRoomId(roomId: RoomId): Set<Session>?
}
