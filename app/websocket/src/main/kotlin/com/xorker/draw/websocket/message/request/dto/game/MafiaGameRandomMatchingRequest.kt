package com.xorker.draw.websocket.message.request.dto.game

data class MafiaGameRandomMatchingRequest(
    val accessToken: String,
    val nickname: String,
    val locale: String,
)
