package com.xorker.draw.websocket.dto

import com.xorker.draw.room.RoomId
import org.springframework.web.socket.WebSocketSession

class WebSocketSessionWrapper(
    private val session: WebSocketSession,
    val roomId: RoomId,
) : WebSocketSession by session
