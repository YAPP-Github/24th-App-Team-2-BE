package com.xorker.draw.timer

import com.xorker.draw.mafia.event.JobWithStartTime
import java.time.Duration

interface TimerRepository {
    fun <T : Any> startTimer(interval: Duration, event: T): JobWithStartTime

    fun startTimer(interval: Duration, callback: () -> Unit): JobWithStartTime
}
