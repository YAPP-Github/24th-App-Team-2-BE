package com.xorker.draw.websocket.session

import com.xorker.draw.auth.token.TokenUseCase
import com.xorker.draw.exception.UnAuthenticationException
import com.xorker.draw.user.User
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.WaitingQueueSession
import com.xorker.draw.websocket.message.request.mafia.MafiaGameRandomMatchingRequest
import com.xorker.draw.websocket.message.request.mafia.SessionInitializeRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
internal class SessionFactory(
    private val tokenUseCase: TokenUseCase,
) {

    fun create(session: WebSocketSession, request: MafiaGameRandomMatchingRequest): WaitingQueueSession {
        return WaitingQueueSessionWrapper(
            session,
            User(getUserId(request.accessToken), request.nickname),
            request.locale,
        )
    }

    fun create(session: WebSocketSession, request: SessionInitializeRequest): Session {
        return SessionWrapper(
            session,
            User(getUserId(request.accessToken), request.nickname),
        )
    }

    private fun getUserId(token: String): UserId {
        return tokenUseCase.getUserId(token) ?: throw UnAuthenticationException
    }
}
