package com.xorker.draw.websocket.message.request.dto

import com.xorker.draw.exception.ExceptionResponse
import com.xorker.draw.websocket.SessionMessage
import com.xorker.draw.websocket.message.request.RequestAction

class ExceptionMessage(
    val action: RequestAction,
    val body: ExceptionResponse,
) {
    val status: SessionMessage.Status = SessionMessage.Status.ERROR
}
