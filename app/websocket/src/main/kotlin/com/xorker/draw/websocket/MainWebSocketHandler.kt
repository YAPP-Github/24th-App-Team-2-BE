package com.xorker.draw.websocket

import com.xorker.draw.room.RoomId
import com.xorker.draw.room.RoomService
import com.xorker.draw.user.UserId
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
    private val roomService: RoomService,
    private val router: WebSocketRouter,
    private val requestParser: WebSocketRequestParser,
) : TextWebSocketHandler() {
    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessionManager.startSession(session)

        val queries = session.getQueries()
        val roomId = queries["roomId"]
        val userId = session.getUserId()

        if (roomId == null) {
            roomService.createRoom(session.id, userId)
        } else {
            roomService.join(RoomId(roomId), session.id, userId)
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        router.route(session, requestParser.parse(message.payload))
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessionManager.endSession(session)
        roomService.exit(session.id)
    }

    private fun WebSocketSession.getUserId(): UserId {
        return UserId(0)
    }

    private fun WebSocketSession.getQueries(): Map<String, String> {
        val pairs = uri?.query?.split("&") ?: return emptyMap()

        return pairs.associate { pair ->
            val index = pair.indexOf("=")
            URLDecoder.decode(pair.substring(0, index), "UTF-8") to URLDecoder.decode(pair.substring(index + 1), "UTF-8")
        }
    }
}
