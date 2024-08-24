package com.xorker.draw.support.logging

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.contrib.jackson.JacksonJsonFormatter
import ch.qos.logback.contrib.json.classic.JsonLayout
import org.slf4j.MDC

class XorkerJsonLayout : JsonLayout() {

    init {
        setTimestampFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        setTimestampFormatTimezoneId("Asia/Seoul")
        appendLineSeparator = true

        jsonFormatter = JacksonJsonFormatter().apply {
            this.isPrettyPrint = false
        }

        includeMDC = false
        includeContextName = false
    }

    override fun addCustomDataToJsonMap(map: MutableMap<String, Any>, event: ILoggingEvent) {
        super.addCustomDataToJsonMap(map, event)

        map["msg"] = map.remove("message") as String

        MDC_LIST.forEach { key ->
            MDC.get(key)?.let { value -> map[key] = value }
        }
    }

    companion object {
        // MDC에서 Json에 넣을 목록
        private val MDC_LIST = listOf("requestId", "userId", "roomId")
    }
}
