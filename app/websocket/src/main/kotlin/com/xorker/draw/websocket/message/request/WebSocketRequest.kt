package com.xorker.draw.websocket.message.request

data class WebSocketRequest(
    val action: RequestAction,
    val body: String?,
)
