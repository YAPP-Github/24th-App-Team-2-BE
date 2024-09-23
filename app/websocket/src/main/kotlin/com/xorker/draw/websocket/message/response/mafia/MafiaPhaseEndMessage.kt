package com.xorker.draw.websocket.message.response.mafia

import com.xorker.draw.websocket.message.response.ResponseAction
import com.xorker.draw.websocket.message.response.SessionMessage
import java.time.LocalDateTime

data class MafiaPhaseEndMessage(
    override val body: MafiaPhaseEndBody,
) : SessionMessage {
    override val action = ResponseAction.PHASE_END
    override val status = SessionMessage.Status.OK
}

data class MafiaPhaseEndBody(
    val startTime: LocalDateTime,
    val mafiaGameInfo: MafiaGameInfoMessage? = null,
    val showAnswer: Boolean,
    val mafiaAnswer: String?,
    val isMafiaWin: Boolean,
    val draw: List<Map<String, Any>>,
)
