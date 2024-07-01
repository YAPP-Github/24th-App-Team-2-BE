package com.xorker.draw.websocket.dto

data class WebSocketResponse(
    val action: ResponseAction,
    val body: Any,
    val status: Status = Status.OK,
) {
    enum class Status {
        OK,
        ERROR,
    }
}
