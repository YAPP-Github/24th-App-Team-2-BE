package com.xorker.draw.mafia.event

import java.time.LocalDateTime
import kotlinx.coroutines.Job

class JobWithStartTime(
    private val job: Job = Job(),
    val startTime: LocalDateTime = LocalDateTime.now(),
) : Job by job
