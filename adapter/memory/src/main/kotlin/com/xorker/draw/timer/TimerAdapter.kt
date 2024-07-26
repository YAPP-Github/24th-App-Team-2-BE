package com.xorker.draw.timer

import java.time.Duration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
internal class TimerAdapter(
    private val eventPublisher: ApplicationEventPublisher,
) : TimerRepository {

    override fun <T : Any> startTimer(interval: Duration, event: T): Job {
        val job = Job()
        CoroutineScope(job + Dispatchers.IO).launch {
            delay(interval.toMillis())
            eventPublisher.publishEvent(event)
        }
        return job
    }
}
