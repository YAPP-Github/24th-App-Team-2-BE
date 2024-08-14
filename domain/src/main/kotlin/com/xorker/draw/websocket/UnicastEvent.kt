package com.xorker.draw.websocket

import com.xorker.draw.user.UserId

data class UnicastEvent(
    val userId: UserId,
    val message: SessionMessage,
)
