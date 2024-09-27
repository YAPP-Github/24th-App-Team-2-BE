package com.xorker.draw.timer

import com.xorker.draw.exception.UnSupportedException
import com.xorker.draw.mafia.event.JobWithStartTime
import com.xorker.draw.room.RoomId
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component

@Component
internal class TimerAdapter : TimerRepository {
    private val jobs = ConcurrentHashMap<RoomId, JobWithStartTime>()

    override fun startTimer(roomId: RoomId, interval: Duration, callback: () -> Unit): JobWithStartTime {
        val job = JobWithStartTime()

        saveJob(roomId, job)

        CoroutineScope(Dispatchers.IO + job).launch {
            delay(interval.toMillis())
            callback.invoke()
        }
        return job
    }

    override fun cancelTimer(roomId: RoomId) {
        jobs[roomId]?.cancel()
        jobs.remove(roomId)
    }

    override fun getTimerStartTime(roomId: RoomId): LocalDateTime {
        val job = jobs[roomId] ?: throw UnSupportedException

        return job.startTime
    }

    private fun saveJob(roomId: RoomId, job: JobWithStartTime) {
        jobs[roomId] = job
    }
}
