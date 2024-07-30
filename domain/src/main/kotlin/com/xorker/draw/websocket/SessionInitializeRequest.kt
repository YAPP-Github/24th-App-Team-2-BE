package com.xorker.draw.websocket

data class SessionInitializeRequest(
    val accessToken: String,
    val roomId: String?,
    val nickname: String,
)
