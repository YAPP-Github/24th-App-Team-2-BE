package com.xorker.draw.websocket.message.response.dto.phase

import com.xorker.draw.websocket.ResponseAction
import com.xorker.draw.websocket.SessionMessage
import java.time.LocalDateTime

data class MafiaPhaseInferAnswerMessage(
    override val body: MafiaPhaseInferAnswerBody,
) : SessionMessage {
    override val action = ResponseAction.PHASE_INFER_ANSWER
    override val status = SessionMessage.Status.OK
}

data class MafiaPhaseInferAnswerBody(
    val startTime: LocalDateTime,
    val mafiaGameInfo: MafiaGameInfoMessage? = null,
    val mafiaAnswer: String?,
    val draw: List<Map<String, Any>>,
)
