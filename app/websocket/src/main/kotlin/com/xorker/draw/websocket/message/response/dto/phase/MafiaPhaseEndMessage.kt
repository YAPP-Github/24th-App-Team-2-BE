package com.xorker.draw.websocket.message.response.dto.phase

import com.xorker.draw.websocket.ResponseAction
import com.xorker.draw.websocket.SessionMessage
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
