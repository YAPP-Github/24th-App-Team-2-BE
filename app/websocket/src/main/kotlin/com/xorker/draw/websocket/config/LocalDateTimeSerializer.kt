package com.xorker.draw.websocket.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeSerializer : JsonSerializer<LocalDateTime>() {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

    override fun serialize(value: LocalDateTime, gen: JsonGenerator, p2: SerializerProvider) {
        val zonedDateTime: ZonedDateTime = value.atZone(ZoneId.of("Asia/Seoul")) // TODO 서버 인스턴스 Zone 구하기
        gen.writeString(formatter.format(zonedDateTime))
    }
}
