package com.xorker.draw.mafia.event

import com.xorker.draw.websocket.WaitingQueueSession

data class MafiaGameRandomMatchingEvent(
    val players: List<WaitingQueueSession>,
)
