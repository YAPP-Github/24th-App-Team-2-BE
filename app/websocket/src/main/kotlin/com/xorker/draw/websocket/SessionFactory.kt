package com.xorker.draw.websocket

import com.xorker.draw.auth.token.TokenUseCase
import com.xorker.draw.exception.UnAuthenticationException
import com.xorker.draw.room.RoomId
import com.xorker.draw.room.RoomRepository
import com.xorker.draw.user.User
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.message.request.SessionWrapper
import com.xorker.draw.websocket.message.request.WaitingQueueSessionWrapper
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
internal class SessionFactory(
    private val tokenUseCase: TokenUseCase,
    private val roomRepository: RoomRepository,
) {

    internal fun create(session: WebSocketSession, request: MafiaGameRandomMatchingRequest): WaitingQueueSession {
        return WaitingQueueSessionWrapper(
            session,
            User(getUserId(request.accessToken), request.nickname),
            request.locale,
        )
    }

    internal fun create(session: WebSocketSession, request: SessionInitializeRequest): Session {
        return SessionWrapper(
            session,
            RoomId(request.roomId?.uppercase() ?: generateRoomId()),
            User(getUserId(request.accessToken), request.nickname),
        )
    }

    internal fun generateRoomId(): String {
        var value: String

        do {
            val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
            value = (1..6)
                .map { charset.random() }
                .joinToString("")
        } while (roomRepository.getRoom(RoomId(value)) != null)

        return value
    }

    private fun getUserId(token: String): UserId {
        return tokenUseCase.getUserId(token) ?: throw UnAuthenticationException
    }
}
