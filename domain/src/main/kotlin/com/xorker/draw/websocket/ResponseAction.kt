package com.xorker.draw.websocket

enum class ResponseAction(
    description: String,
) {
    PLAYER_LIST("대기방 갱신"),
    GAME_INFO("마피아 게임 정보"),
    GAME_READY("마피아 게임 준비"),
    PLAYER_TURN_LIST("플레이어 턴 순서"),
    DRAW("그림 데이터"),
    TURN_INFO("새로운 턴 시작"),
    VOTE_STATUS("마피아 투표 현황"),
    ANSWER("마피아 입력 키워드"),

    PHASE_WAIT("Wait Phase 시작/초기화"),
    PHASE_READY("Ready Phase 시작/초기화"),
    PHASE_PLAYING("Player Phase 시작/초기화"),
    PHASE_VOTE("Vote Phase 시작/초기화"),
    PHASE_INFER_ANSWER("Infer Answer 시작/초기화"),
    PHASE_END("End 시작/초기화"),
}
