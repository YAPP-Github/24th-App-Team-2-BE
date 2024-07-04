package com.xorker.draw.room

interface RoomPort {
    fun saveRoom(room: Room)
    fun getRoom(roomId: RoomId): Room?
}
