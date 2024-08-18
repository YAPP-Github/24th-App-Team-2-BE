package com.xorker.draw.support.logging

import java.util.UUID
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC

inline fun <reified T> T.logger(): Logger = LoggerFactory.getLogger(T::class.java)

fun registerRequestId() {
    MDC.put("requestId", UUID.randomUUID().toString())
}
