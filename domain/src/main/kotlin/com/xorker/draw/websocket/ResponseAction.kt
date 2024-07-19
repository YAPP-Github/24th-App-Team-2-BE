package com.xorker.draw.websocket

enum class ResponseAction(
    description: String,
) {
    PLAYER_LIST("대기방 갱신"),
    GAME_INFO("마피아 게임 정보"),
    GAME_READY("마피아 게임 준비"),
}
