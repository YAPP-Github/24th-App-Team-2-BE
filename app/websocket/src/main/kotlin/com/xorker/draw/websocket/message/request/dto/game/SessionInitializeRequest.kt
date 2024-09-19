package com.xorker.draw.websocket.message.request.dto.game

data class SessionInitializeRequest(
    val accessToken: String,
    val roomId: String?,
    val nickname: String,
    val locale: String,
)
