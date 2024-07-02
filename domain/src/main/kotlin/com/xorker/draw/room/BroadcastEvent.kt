package com.xorker.draw.room

data class BroadcastEvent(
    val roomId: RoomId,
    val action: Action,
    val body: Any,
    val status: Status = Status.OK,
) {
    enum class Status {
        OK,
        ERROR,
    }
}
