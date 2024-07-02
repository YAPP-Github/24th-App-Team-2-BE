package com.xorker.draw.websocket.dto

enum class RequestAction(
    description: String,
) {
    SESSION_CONNECT("세션이 연결 됨"),
    SESSION_DISCONNECT("세션 연결이 종료 됨"),
    TEST("테스트용"),
}
