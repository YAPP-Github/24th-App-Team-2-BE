package com.xorker.draw.timer

import com.xorker.draw.mafia.event.JobWithStartTime
import com.xorker.draw.room.RoomId
import java.time.Duration
import java.time.LocalDateTime

interface TimerRepository {
    fun startTimer(roomId: RoomId, interval: Duration, callback: () -> Unit): JobWithStartTime
    fun cancelTimer(roomId: RoomId)
    fun getTimerStartTime(roomId: RoomId): LocalDateTime?
}
