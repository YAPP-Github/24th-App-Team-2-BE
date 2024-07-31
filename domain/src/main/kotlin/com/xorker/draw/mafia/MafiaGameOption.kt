package com.xorker.draw.mafia

import java.time.Duration

data class MafiaGameOption(
    val minimum: Int = 3,
    val maximum: Int = 10,
    val readyTime: Duration = Duration.ofSeconds(10), // 마피아 게임 준비 시간
    val introAnimationTime: Duration = Duration.ofMillis(3000), // Intro 애니메이션 시간
    val roundAnimationTime: Duration = Duration.ofMillis(1650), // Round 애니메이션 시간
    val round: Int = 2, // 전체 라운드 수
    val turnTime: Duration = Duration.ofSeconds(5), // 턴 당 최대 시간
    val turnCount: Int = 2, // 턴 당 최대 획 수
    val voteTime: Duration = Duration.ofSeconds(10), // 투표 시간
    val answerTime: Duration = Duration.ofSeconds(10), // 정답 입력 시간
)
