package com.xorker.draw.room

import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.SessionId

abstract class Player(
    val userId: UserId,
    nickname: String,
) {
    var nickname: String = nickname
        private set
    private var connectStatus: ConnectStatus = ConnectStatus.CONNECTED

    fun connect() {
        this.connectStatus = ConnectStatus.CONNECTED
    }

    fun disconnect() {
        this.connectStatus = ConnectStatus.DISCONNECTED
    }

    private enum class ConnectStatus {
        CONNECTED,
        DISCONNECTED,
    }
}
