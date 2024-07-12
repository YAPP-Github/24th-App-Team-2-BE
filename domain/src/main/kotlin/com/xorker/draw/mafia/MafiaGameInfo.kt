package com.xorker.draw.mafia

import com.xorker.draw.room.Room

class MafiaGameInfo(
    val room: Room<MafiaPlayer>,
    val phase: MafiaPhase,
    val gameOption: MafiaGameOption,
) : Room<MafiaPlayer> by room
