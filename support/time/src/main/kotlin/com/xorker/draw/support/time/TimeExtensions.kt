package com.xorker.draw.support.time

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun generateUtc(): String {
    val now = LocalDateTime.now()

    val timeZone = TimeZone.getTimeZone("UTC")
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm.ss.SSS'Z'")
    dateFormat.timeZone = timeZone

    return dateFormat.format(now.toDate())
}

fun LocalDateTime.toDate(): Date {
    val instant = this.atZone(ZoneId.systemDefault()).toInstant()
    return Date.from(instant)
}
