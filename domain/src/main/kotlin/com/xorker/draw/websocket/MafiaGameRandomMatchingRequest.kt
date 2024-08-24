package com.xorker.draw.websocket

data class MafiaGameRandomMatchingRequest(
    val accessToken: String,
    val nickname: String,
    val locale: String,
)
