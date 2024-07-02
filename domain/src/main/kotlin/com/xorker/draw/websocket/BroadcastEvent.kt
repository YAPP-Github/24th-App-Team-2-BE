package com.xorker.draw.websocket

data class BroadcastEvent(
    val sessionId: String,
    val action: Action,
    val body: Any,
    val status: Status = Status.OK,
) {
    enum class Status {
        OK,
        ERROR,
    }
}
