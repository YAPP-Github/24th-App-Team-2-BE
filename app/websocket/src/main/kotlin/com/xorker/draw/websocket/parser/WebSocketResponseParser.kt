package com.xorker.draw.websocket.parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.xorker.draw.websocket.dto.WebSocketResponse
import org.springframework.stereotype.Component

@Component
class WebSocketResponseParser(
    private val objectMapper: ObjectMapper,
) {

    fun parse(response: WebSocketResponse): String =
        StringBuilder()
            .append(response.status)
            .append("\n")
            .append(response.action)
            .append("\n")
            .append(objectMapper.writeValueAsString(response.body))
            .toString()
}
