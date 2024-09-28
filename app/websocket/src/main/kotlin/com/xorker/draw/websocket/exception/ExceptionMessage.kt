package com.xorker.draw.websocket.exception

import com.xorker.draw.exception.ExceptionResponse
import com.xorker.draw.websocket.message.request.RequestAction
import com.xorker.draw.websocket.message.response.SessionMessage

internal class ExceptionMessage(
    val action: RequestAction,
    val body: ExceptionResponse,
) {
    val status: SessionMessage.Status = SessionMessage.Status.ERROR
}
