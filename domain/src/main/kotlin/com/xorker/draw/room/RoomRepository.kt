package com.xorker.draw.room

interface RoomRepository {
    fun <P : Player> saveRoom(room: Room<P>)
    fun getRoom(roomId: RoomId): Room<Player>?
}
