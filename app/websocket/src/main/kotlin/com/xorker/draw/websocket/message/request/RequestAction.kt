package com.xorker.draw.websocket.message.request

enum class RequestAction(
    description: String,
) {
    INIT("세션 초기화"),
    START_GAME("마피아 게임 시작"),
}
