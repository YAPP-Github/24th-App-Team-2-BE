package com.xorker.draw.websocket

import com.xorker.draw.auth.token.TokenUseCase
import com.xorker.draw.exception.UnAuthenticationException
import com.xorker.draw.room.RoomId
import com.xorker.draw.room.RoomRepository
import com.xorker.draw.user.User
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.dto.SessionInitializeRequest
import com.xorker.draw.websocket.dto.SessionWrapper
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession

@Service
class SessionFactory(
    private val tokenUseCase: TokenUseCase,
    private val roomRepository: RoomRepository,
) {
    fun create(session: WebSocketSession, request: SessionInitializeRequest): Session {
        return SessionWrapper(
            session,
            RoomId(request.roomId?.uppercase() ?: generateRoomId()),
            User(getUserId(request.accessToken), request.nickname),
        )
    }

    private fun generateRoomId(): String {
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
