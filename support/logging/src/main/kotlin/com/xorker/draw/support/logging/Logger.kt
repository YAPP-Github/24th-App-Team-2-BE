package com.xorker.draw.support.logging

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC

inline fun <reified T> T.logger(): Logger = LoggerFactory.getLogger(T::class.java)

fun registerRequestId() {
    MDC.put("requestId", UUID.randomUUID().toString())
}

private val TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
fun defaultApiJsonMap(vararg pairs: Pair<String, Any?>): MutableMap<String, Any?> {
    val now = ZonedDateTime.now()

    val map = mutableMapOf(*pairs)

    map["timestamp"] = TIMESTAMP_FORMATTER.format(now)
    map["requestId"] = MDC.get("requestId")
    map["userId"] = MDC.get("userId")

    return map
}
