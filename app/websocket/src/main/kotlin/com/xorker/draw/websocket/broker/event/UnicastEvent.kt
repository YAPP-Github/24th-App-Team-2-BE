package com.xorker.draw.websocket.broker.event

import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.message.response.SessionMessage

data class UnicastEvent(
    val userId: UserId,
    val message: SessionMessage,
)
