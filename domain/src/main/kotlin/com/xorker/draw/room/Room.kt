package com.xorker.draw.room

import com.xorker.draw.websocket.Session

@JvmInline
value class RoomId(val value: String)

data class Room(
    val id: RoomId,
    private val _sessions: MutableSet<Session> = mutableSetOf(),
) {
    val sessions: Set<Session> = _sessions

    fun size(): Int = _sessions.size
    fun isEmpty(): Boolean = size() == 0

    fun put(session: Session) {
        _sessions.add(session)
    }

    fun remove(session: Session) {
        _sessions.remove(session)
    }
}
