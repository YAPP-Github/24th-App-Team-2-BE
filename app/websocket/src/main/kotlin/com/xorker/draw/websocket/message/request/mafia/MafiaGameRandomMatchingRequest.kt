package com.xorker.draw.websocket.message.request.mafia

data class MafiaGameRandomMatchingRequest(
    val accessToken: String,
    val nickname: String,
    val locale: String,
)
