package com.xorker.draw.timer

import com.xorker.draw.mafia.event.JobWithStartTime
import java.time.Duration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
internal class TimerAdapter(
    private val eventPublisher: ApplicationEventPublisher,
) : TimerRepository {

    override fun <T : Any> startTimer(interval: Duration, event: T): JobWithStartTime {
        val job = JobWithStartTime()
        CoroutineScope(Dispatchers.IO + job).launch {
            delay(interval.toMillis())
            eventPublisher.publishEvent(event)
        }
        return job
    }

    override fun startTimer(interval: Duration, callback: () -> Unit): JobWithStartTime {
        val job = JobWithStartTime()
        CoroutineScope(Dispatchers.IO + job).launch {
            delay(interval.toMillis())
            callback.invoke()
        }
        return job
    }
}
