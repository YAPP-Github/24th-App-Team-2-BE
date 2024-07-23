package com.xorker.draw.timer

import java.time.Duration

interface TimerRepository {
    fun <T> startTimer(interval: Duration, event: T)
}
