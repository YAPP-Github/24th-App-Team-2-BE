package com.xorker.draw.websocket.exception

import com.xorker.draw.exception.XorkerException
import com.xorker.draw.exception.XorkerExceptionHandler
import com.xorker.draw.websocket.dto.RequestAction
import com.xorker.draw.websocket.message.dto.ExceptionMessage
import com.xorker.draw.websocket.parser.WebSocketResponseParser
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Component
class WebSocketExceptionHandler(
    private val exceptionHandler: XorkerExceptionHandler,
    private val responseParser: WebSocketResponseParser,
) {
    fun handleXorkerException(session: WebSocketSession, requestAction: RequestAction, ex: XorkerException) {
        // TODO : Logging

        val message = ExceptionMessage(
            requestAction,
            exceptionHandler.convert(ex),
        )

        val messageStr = responseParser.parse(message)

        session.sendMessage(TextMessage(messageStr))
    }
}
