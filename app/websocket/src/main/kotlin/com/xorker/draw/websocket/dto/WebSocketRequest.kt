package com.xorker.draw.websocket.dto

data class WebSocketRequest(
    val action: RequestAction,
    val body: String?,
)
