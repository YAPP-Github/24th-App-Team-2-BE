package com.xorker.draw.websocket.message.request.mafia

data class SessionInitializeRequest(
    val accessToken: String,
    val roomId: String?,
    val nickname: String,
    val locale: String,
)
