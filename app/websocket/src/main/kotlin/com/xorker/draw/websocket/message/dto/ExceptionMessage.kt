package com.xorker.draw.websocket.message.dto

import com.xorker.draw.exception.ExceptionResponse
import com.xorker.draw.websocket.dto.RequestAction
import com.xorker.draw.websocket.message.SessionMessage

class ExceptionMessage(
    val action: RequestAction,
    val body: ExceptionResponse,
) {
    val status: SessionMessage.Status = SessionMessage.Status.ERROR
}
