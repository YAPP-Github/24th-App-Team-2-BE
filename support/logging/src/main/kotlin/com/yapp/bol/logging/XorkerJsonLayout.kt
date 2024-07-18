package com.yapp.bol.logging

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.contrib.jackson.JacksonJsonFormatter
import ch.qos.logback.contrib.json.classic.JsonLayout
import org.slf4j.MDC

class XorkerJsonLayout : JsonLayout() {

    init {
        setTimestampFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        setTimestampFormatTimezoneId("Asia/Seoul")
        appendLineSeparator = true

        jsonFormatter = JacksonJsonFormatter().apply {
            this.isPrettyPrint = false
        }
    }

    override fun addCustomDataToJsonMap(map: MutableMap<String, Any>, p1: ILoggingEvent?) {
        super.addCustomDataToJsonMap(map, p1)

        MDC.get("test")?.let {
            map["test"] = it
        }
    }
}
