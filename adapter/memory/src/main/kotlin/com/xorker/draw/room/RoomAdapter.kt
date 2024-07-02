package com.xorker.draw.room

import com.xorker.draw.websocket.SessionInfo
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Component

private typealias RoomMap = ConcurrentHashMap<RoomId, MutableSet<SessionInfo>>
private typealias SessionMap = ConcurrentHashMap<String, SessionInfo>

@Component
class RoomAdapter : RoomRepository {
    val roomMap = RoomMap()
    val sessionMap = SessionMap()

    override fun joinUser(session: SessionInfo) {
        roomMap.put(session.roomId, session)
        sessionMap[session.sessionId] = session
    }

    override fun exitUser(sessionId: String): RoomId {
        val sessionInfo = sessionMap[sessionId] ?: throw RuntimeException()

        sessionMap.remove(sessionInfo.sessionId)
        roomMap.remove(sessionInfo)

        return sessionInfo.roomId
    }
}

private fun RoomMap.put(roomId: RoomId, session: SessionInfo) {
    val room = this.getOrPut(roomId) {
        Collections.synchronizedSet(mutableSetOf<SessionInfo>())
    }

    room.add(session)
}

private fun RoomMap.remove(session: SessionInfo) {
    val room = this[session.roomId] ?: return
    room.remove(session)

    if (room.size == 0) {
        this.remove(session.roomId)
    }
}
