package com.xorker.draw.mafia

import com.xorker.draw.room.Room

interface MafiaGameMessenger {
    fun broadcastPlayerList(room: Room<MafiaPlayer>)
}
