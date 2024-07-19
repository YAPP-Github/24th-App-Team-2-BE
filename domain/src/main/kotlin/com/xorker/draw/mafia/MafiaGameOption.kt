package com.xorker.draw.mafia

import java.time.Duration

data class MafiaGameOption(
    var turnTime: Duration = Duration.ofSeconds(5),
    var numTurn: Int = 2,
    val roundTime: Int = 1,
)
