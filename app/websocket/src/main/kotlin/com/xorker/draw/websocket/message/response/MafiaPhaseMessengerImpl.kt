package com.xorker.draw.websocket.message.response

import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.mafia.MafiaPhaseMessenger
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.RespectiveBroadcastEvent
import com.xorker.draw.websocket.SessionMessage
import com.xorker.draw.websocket.broker.WebSocketBroadcaster
import com.xorker.draw.websocket.message.response.dto.MafiaGameInfoBody
import com.xorker.draw.websocket.message.response.dto.MafiaGameInfoMessage
import com.xorker.draw.websocket.message.response.dto.MafiaPhaseReadyBody
import com.xorker.draw.websocket.message.response.dto.MafiaPhaseReadyMessage
import com.xorker.draw.websocket.message.response.dto.toResponse
import org.springframework.stereotype.Component

@Component
class MafiaPhaseMessengerImpl(
    private val broadcaster: WebSocketBroadcaster,
) : MafiaPhaseMessenger {
    override fun unicastPhase(userId: UserId, gameInfo: MafiaGameInfo) {
        // broadcaster.unicast(userId, gameInfo.generateMessage())
    }

    override fun broadcastPhase(gameInfo: MafiaGameInfo) {
        val event = RespectiveBroadcastEvent(gameInfo.room.id, gameInfo.generateMessage())

        broadcaster.publishRespectiveBroadcastEvent(event)
    }

    private fun MafiaGameInfo.generateMessage(): Map<UserId, SessionMessage> {
        return when (val phase = this.phase) {
            MafiaPhase.Wait -> TODO()
            is MafiaPhase.Ready -> {
                val messages = mutableMapOf<UserId, SessionMessage>()
                val startTime = phase.job.startTime
                val turnList = phase.turnList
                val mafiaPlayer = phase.mafiaPlayer
                val keyword = phase.keyword

                turnList.forEachIndexed { i, player ->
                    val message = MafiaPhaseReadyMessage(
                        MafiaPhaseReadyBody(
                            startTime = startTime,
                            gameInfo = MafiaGameInfoMessage(
                                MafiaGameInfoBody(
                                    userId = player.userId,
                                    turn = i,
                                    isMafia = mafiaPlayer.userId == player.userId,
                                    turnList = turnList,
                                    category = keyword.category,
                                    answer = keyword.answer,
                                    gameOption = gameOption.toResponse(),
                                ),
                            ),
                        ),
                    )
                    messages[player.userId] = message
                }

                return messages
            }

            is MafiaPhase.Playing -> TODO()
            is MafiaPhase.Vote -> TODO()
            is MafiaPhase.InferAnswer -> TODO()
            is MafiaPhase.End -> TODO()
        }
    }
}
