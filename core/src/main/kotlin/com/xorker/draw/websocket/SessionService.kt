package com.xorker.draw.websocket

import com.xorker.draw.room.RoomId
import com.xorker.draw.room.RoomService
import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Service

@Service
internal class SessionService(
    private val roomService: RoomService,
) : SessionUseCase {
    private val roomMap = ConcurrentHashMap<RoomId, MutableSet<Session>>()
    private val sessionMap: ConcurrentHashMap<SessionId, Session> = ConcurrentHashMap()

    override fun registerSession(session: Session) {
        if (sessionMap.contains(session.id)) {
            // Init을 중복으로 호출 하면 기존 데이터를 Unregister 하고 Init 한다.
            unregisterSession(session.id)
        }

        roomService.connect(session.roomId, session)

        sessionMap[session.id] = session

        val roomId = session.roomId
        val roomEntry = roomMap.getOrPut(roomId) { mutableSetOf() }
        roomEntry.add(session)
    }

    override fun unregisterSession(sessionId: SessionId) {
        val session = sessionMap[sessionId] ?: return
        roomService.disconnect(session.roomId, session)

        sessionMap.remove(session.id)

        val roomEntry = roomMap[session.roomId] ?: return
        roomEntry.remove(session)
        if (roomEntry.isEmpty()) {
            roomMap.remove(session.roomId)
        }
    }

    override fun getSession(sessionId: SessionId): Session? {
        return sessionMap[sessionId]
    }

    override fun getSessionsByRoomId(roomId: RoomId): Set<Session>? {
        return roomMap[roomId]
    }
}
