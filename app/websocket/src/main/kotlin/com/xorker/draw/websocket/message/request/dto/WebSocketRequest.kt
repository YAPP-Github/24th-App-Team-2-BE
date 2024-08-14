package com.xorker.draw.websocket.message.request.dto

import com.xorker.draw.websocket.message.request.RequestAction

data class WebSocketRequest(
    val action: RequestAction,
    val body: String?,
)
