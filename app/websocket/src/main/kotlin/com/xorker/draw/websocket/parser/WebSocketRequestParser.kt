package com.xorker.draw.websocket.parser

import com.xorker.draw.websocket.message.request.RequestAction
import com.xorker.draw.websocket.message.request.dto.WebSocketRequest
import org.springframework.stereotype.Component

@Component
internal class WebSocketRequestParser {

    fun parse(request: String): WebSocketRequest {
        val index = request.indexOf("\n")

        if (index == -1) {
            return WebSocketRequest(RequestAction.valueOf(request), null)
        }

        return WebSocketRequest(
            RequestAction.valueOf(request.substring(0, index)),
            request.substring(index),
        )
    }
}
