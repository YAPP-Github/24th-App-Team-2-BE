package com.xorker.draw.websocket

interface SessionMessage {
    val action: ResponseAction
    val body: Any
    val status: Status

    enum class Status {
        OK,
        ERROR,
    }
}
