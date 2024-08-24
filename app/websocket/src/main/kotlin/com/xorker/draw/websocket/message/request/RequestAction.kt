package com.xorker.draw.websocket.message.request

enum class RequestAction(
    description: String,
) {
    INIT("세션 초기화"),
    RANDOM_MATCHING("마피아 게임 랜덤 매칭"),
    START_GAME("마피아 게임 시작"),
    DRAW("그림 그리기"),
    END_TURN("턴 넘기기"),
    VOTE("마피아 투표"),
    ANSWER("마피아 정답 입력"),
    DECIDE_ANSWER("마피아 정답 확정"),
}
