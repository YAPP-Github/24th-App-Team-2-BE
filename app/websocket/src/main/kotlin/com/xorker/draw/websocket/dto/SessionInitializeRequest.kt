package com.xorker.draw.websocket.dto

data class SessionInitializeRequest(
    val accessToken: String,
    val roomId: String?,
    val nickname: String,
)
