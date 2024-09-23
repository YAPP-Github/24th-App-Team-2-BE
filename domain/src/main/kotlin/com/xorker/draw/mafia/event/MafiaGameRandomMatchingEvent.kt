package com.xorker.draw.mafia.event

import com.xorker.draw.user.User

data class MafiaGameRandomMatchingEvent(
    val players: List<User>,
    val locale: String,
)
