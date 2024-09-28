package com.xorker.draw.websocket.message.response

import com.fasterxml.jackson.databind.ObjectMapper
import com.xorker.draw.websocket.exception.ExceptionMessage
import org.springframework.stereotype.Component

@Component
internal class WebSocketResponseParser(
    private val objectMapper: ObjectMapper,
) {

    fun parse(response: SessionMessage): String =
        StringBuilder()
            .append(response.status)
            .append("\n")
            .append(response.action)
            .append("\n")
            .append(objectMapper.writeValueAsString(response.body))
            .toString()

    fun parse(exceptionMessage: ExceptionMessage): String =
        StringBuilder()
            .append(exceptionMessage.status)
            .append("\n")
            .append(exceptionMessage.action)
            .append("\n")
            .append(objectMapper.writeValueAsString(exceptionMessage.body))
            .toString()
}
