package com.xorker.draw.websocket

data class SessionMessage(
    val action: Action,
    val body: Any,
    val status: Status = Status.OK,
) {
    enum class Status {
        OK,
        ERROR,
    }
}
