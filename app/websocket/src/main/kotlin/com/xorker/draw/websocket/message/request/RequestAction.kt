package com.xorker.draw.websocket.message.request

enum class RequestAction(
    description: String,
) {
    INIT("세션 초기화"),
    START_GAME("마피아 게임 시작"),
    DRAW("그림 그리기"),
    VOTE("마피아 투표"),
}
