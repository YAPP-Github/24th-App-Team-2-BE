package com.xorker.draw.websocket.message.request.dto

data class SessionInitializeRequest(
    val accessToken: String,
    val roomId: String?,
    val nickname: String,
)
