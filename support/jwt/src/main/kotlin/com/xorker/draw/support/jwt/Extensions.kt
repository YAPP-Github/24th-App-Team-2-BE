package com.xorker.draw.support.jwt

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun LocalDateTime.toDate(): Date {
    val instant = this.atZone(ZoneId.systemDefault()).toInstant()
    return Date.from(instant)
}
