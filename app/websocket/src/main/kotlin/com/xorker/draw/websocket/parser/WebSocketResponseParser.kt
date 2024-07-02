package com.xorker.draw.websocket.parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.xorker.draw.websocket.BroadcastEvent
import org.springframework.stereotype.Component

@Component
class WebSocketResponseParser(
    private val objectMapper: ObjectMapper,
) {

    fun parse(event: BroadcastEvent): String =
        StringBuilder()
            .append(event.status)
            .append("\n")
            .append(event.action)
            .append("\n")
            .append(objectMapper.writeValueAsString(event.body))
            .toString()
}
