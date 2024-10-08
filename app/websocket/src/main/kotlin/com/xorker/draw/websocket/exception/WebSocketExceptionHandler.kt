package com.xorker.draw.websocket.exception

import com.xorker.draw.exception.CriticalException
import com.xorker.draw.exception.ServerException
import com.xorker.draw.exception.XorkerException
import com.xorker.draw.exception.XorkerExceptionHandler
import com.xorker.draw.support.logging.logger
import com.xorker.draw.websocket.message.request.RequestAction
import com.xorker.draw.websocket.parser.WebSocketResponseParser
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Component
internal class WebSocketExceptionHandler(
    private val exceptionHandler: XorkerExceptionHandler,
    private val responseParser: WebSocketResponseParser,
) {
    private val logger = logger()

    fun handleXorkerException(session: WebSocketSession, requestAction: RequestAction, ex: XorkerException) {
        logXorkerException(ex)

        val message = ExceptionMessage(
            requestAction,
            exceptionHandler.convert(ex),
        )

        val messageStr = responseParser.parse(message)

        session.sendMessage(TextMessage(messageStr))
    }

    private fun logXorkerException(ex: XorkerException) {
        when (ex) {
            is ServerException, is CriticalException -> {
                logger.error(ex.message, ex)
            }

            else -> logger.warn(ex.message, ex)
        }
    }
}
