package com.xorker.draw.mafia

import java.time.Duration

data class MafiaGameOption(
    var turnTime: Duration = Duration.ofSeconds(5), // 턴 당 최대 시간
    var numTurn: Int = 2, // 라운드 횟수
    val readyShowingTime: Duration = Duration.ofSeconds(7), // 준비 시간
    val roleShowingTime: Duration = Duration.ofSeconds(3), // 각자 턴 순서 시간
    val roundShowingTime: Duration = Duration.ofSeconds(2), // 라운드 시간
)
