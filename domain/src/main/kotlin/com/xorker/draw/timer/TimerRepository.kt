package com.xorker.draw.timer

import java.time.Duration
import kotlinx.coroutines.Job

interface TimerRepository {
    fun <T : Any> startTimer(interval: Duration, event: T): Job
}
