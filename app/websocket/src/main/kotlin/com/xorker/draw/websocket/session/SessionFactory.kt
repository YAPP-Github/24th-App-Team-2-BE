package com.xorker.draw.websocket.session

import com.xorker.draw.auth.token.TokenUseCase
import com.xorker.draw.exception.UnAuthenticationException
import com.xorker.draw.room.RoomId
import com.xorker.draw.room.RoomRepository
import com.xorker.draw.user.User
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.Session
import com.xorker.draw.websocket.WaitingQueueSession
import com.xorker.draw.websocket.message.request.SessionWrapper
import com.xorker.draw.websocket.message.request.WaitingQueueSessionWrapper
import com.xorker.draw.websocket.message.request.dto.game.MafiaGameRandomMatchingRequest
import com.xorker.draw.websocket.message.request.dto.game.SessionInitializeRequest
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
internal class SessionFactory(
    private val tokenUseCase: TokenUseCase,
    private val roomRepository: RoomRepository,
) {

    fun create(session: WebSocketSession, request: MafiaGameRandomMatchingRequest): WaitingQueueSession {
        return WaitingQueueSessionWrapper(
            session,
            User(getUserId(request.accessToken), request.nickname),
            request.locale,
        )
    }

    fun create(session: WebSocketSession, request: SessionInitializeRequest): Session {
        val roomId = RoomId(request.roomId?.uppercase() ?: generateRoomId())

        MDC.put("roomId", roomId.value)

        return SessionWrapper(
            session,
            roomId,
            User(getUserId(request.accessToken), request.nickname),
        )
    }

    fun generateRoomId(): String {
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
