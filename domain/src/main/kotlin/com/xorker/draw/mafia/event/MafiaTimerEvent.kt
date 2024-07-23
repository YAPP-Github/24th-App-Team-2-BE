package com.xorker.draw.mafia.event

import com.xorker.draw.mafia.MafiaGameInfo

sealed class MafiaTimerEvent

data class MafiaReadyExpiredEvent(
    val gameInfo: MafiaGameInfo,
) : MafiaTimerEvent()

data class MafiaRoleExpiredEvent(
    val gameInfo: MafiaGameInfo,
) : MafiaTimerEvent()

data class MafiaRoundExpiredEvent(
    val gameInfo: MafiaGameInfo,
) : MafiaTimerEvent()

data class MafiaTurnExpiredEvent(
    val gameInfo: MafiaGameInfo,
) : MafiaTimerEvent()
