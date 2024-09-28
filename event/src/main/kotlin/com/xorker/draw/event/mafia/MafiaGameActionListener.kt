package com.xorker.draw.event.mafia

import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaReactionType
import com.xorker.draw.room.RoomId

interface MafiaGameActionListener {
    fun draw(roomId: RoomId, drawData: Map<String, Any>)
    fun nextTurn(gameInfo: MafiaGameInfo)
    fun vote(gameInfo: MafiaGameInfo)
    fun answer(gameInfo: MafiaGameInfo, answer: String)
    fun reaction(gameInfo: MafiaGameInfo, reaction: MafiaReactionType)
}
