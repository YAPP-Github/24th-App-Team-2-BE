package com.xorker.draw.websocket

data class SessionMessage(
    val action: Action,
    val status: Status = Status.OK,
    val body: Any,
) {
    enum class Status {
        OK,
        ERROR,
    }
}
