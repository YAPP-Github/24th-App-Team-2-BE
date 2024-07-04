package com.xorker.draw.room

import com.xorker.draw.websocket.Session

@JvmInline
value class RoomId(val value: String)

data class Room(
    val id: RoomId,
) {
    private val sessions: MutableSet<Session> = mutableSetOf()

    fun size(): Int = sessions.size
    fun isEmpty(): Boolean = size() == 0

    fun put(session: Session) {
        sessions.add(session)
    }

    fun remove(session: Session) {
        sessions.remove(session)
    }
}
