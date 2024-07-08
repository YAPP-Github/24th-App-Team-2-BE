package com.xorker.draw.room

interface RoomUseCase {
    fun getRoom(roomId: RoomId): Room?
}
