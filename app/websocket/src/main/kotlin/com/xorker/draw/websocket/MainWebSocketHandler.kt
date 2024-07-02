package com.xorker.draw.websocket

import com.xorker.draw.room.RoomId
import com.xorker.draw.websocket.dto.RequestAction
import com.xorker.draw.websocket.dto.WebSocketRequest
import com.xorker.draw.websocket.dto.WebSocketSessionWrapper
import com.xorker.draw.websocket.parser.WebSocketRequestParser
import java.net.URLDecoder
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class MainWebSocketHandler(
    private val sessionManager: WebSocketSessionManager,
    private val router: WebSocketRouter,
    private val requestParser: WebSocketRequestParser,
) : TextWebSocketHandler() {
    override fun afterConnectionEstablished(session: WebSocketSession) {
        val queries = session.getQueries()
        val roomId = queries.roomId()

        val wrapper = WebSocketSessionWrapper(session, roomId)

        sessionManager.startSession(wrapper)
        router.route(WebSocketRequest(RequestAction.SESSION_CONNECT))
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val request = requestParser.parse(message.payload)

        router.route(request)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessionManager.endSession(session)
        router.route(WebSocketRequest(RequestAction.SESSION_DISCONNECT))
    }

    private fun WebSocketSession.getQueries(): Map<String, String> {
        val pairs = uri?.query?.split("&") ?: return emptyMap()

        return pairs.associate { pair ->
            val index = pair.indexOf("=")
            URLDecoder.decode(pair.substring(0, index), "UTF-8") to URLDecoder.decode(pair.substring(index + 1), "UTF-8")
        }
    }

    private fun Map<String, String>.roomId(): RoomId {
        val value = this["roomId"]
        return if (value == null) {
            sessionManager.generateRoomId()
        } else {
            RoomId(value)
        }
    }
}
