package com.yapp.bol.logging

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.contrib.jackson.JacksonJsonFormatter
import ch.qos.logback.contrib.json.classic.JsonLayout

class XorkerJsonLayout : JsonLayout() {

    init {
        setTimestampFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        setTimestampFormatTimezoneId("Asia/Seoul")
        appendLineSeparator = true

        jsonFormatter = JacksonJsonFormatter().apply {
            this.isPrettyPrint = false
        }
    }

    override fun addCustomDataToJsonMap(map: MutableMap<String, Any>, event: ILoggingEvent) {
        super.addCustomDataToJsonMap(map, event)
    }
}
