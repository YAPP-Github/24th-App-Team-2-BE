package com.xorker.draw.mafia

import java.time.Duration

data class MafiaGameOption(
    val minimum: Int = 3,
    val maximum: Int = 10,
    val readyTime: Duration = Duration.ofSeconds(10), // 마피아 게임 준비 시간
    val animationTime: Duration = Duration.ofSeconds(2), // 애니메이션 시간
    val round: Int = 2, // 전체 라운드 수
    val turnTime: Duration = Duration.ofSeconds(5), // 턴 당 최대 시간
    val turnCount: Int = 2, // 턴 당 최대 획 수
    val voteTime: Duration = Duration.ofMinutes(10), // 투표 시간
    val answerTime: Duration = Duration.ofMinutes(10), // 정답 입력 시간
)
