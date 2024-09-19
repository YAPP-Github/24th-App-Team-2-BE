package com.xorker.draw.websocket.message.response

import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaGameOption
import com.xorker.draw.mafia.MafiaKeyword
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.mafia.MafiaPhaseMessenger
import com.xorker.draw.mafia.MafiaPlayer
import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.broker.WebSocketBroadcaster
import com.xorker.draw.websocket.message.response.dto.game.toResponse
import com.xorker.draw.websocket.message.response.dto.phase.MafiaGameInfoBody
import com.xorker.draw.websocket.message.response.dto.phase.MafiaGameInfoMessage
import com.xorker.draw.websocket.message.response.dto.phase.MafiaPhaseEndBody
import com.xorker.draw.websocket.message.response.dto.phase.MafiaPhaseEndMessage
import com.xorker.draw.websocket.message.response.dto.phase.MafiaPhaseInferAnswerBody
import com.xorker.draw.websocket.message.response.dto.phase.MafiaPhaseInferAnswerMessage
import com.xorker.draw.websocket.message.response.dto.phase.MafiaPhasePlayingBody
import com.xorker.draw.websocket.message.response.dto.phase.MafiaPhasePlayingMessage
import com.xorker.draw.websocket.message.response.dto.phase.MafiaPhaseReadyBody
import com.xorker.draw.websocket.message.response.dto.phase.MafiaPhaseReadyMessage
import com.xorker.draw.websocket.message.response.dto.phase.MafiaPhaseVoteBody
import com.xorker.draw.websocket.message.response.dto.phase.MafiaPhaseVoteMessage
import com.xorker.draw.websocket.message.response.dto.phase.MafiaPhaseWaitBody
import com.xorker.draw.websocket.message.response.dto.phase.MafiaPhaseWaitMessage
import com.xorker.draw.websocket.message.response.dto.phase.toResponse
import org.springframework.stereotype.Component

@Component
internal class MafiaPhaseMessengerImpl(
    private val broadcaster: WebSocketBroadcaster,
) : MafiaPhaseMessenger {

    override fun unicastPhase(userId: UserId, gameInfo: MafiaGameInfo) {
        broadcaster.unicast(userId, gameInfo.generateMessage(false))
    }

    override fun broadcastPhase(gameInfo: MafiaGameInfo) {
        val room = gameInfo.room

        broadcaster.broadcast(room.id, gameInfo.generateMessage())
    }

    private fun MafiaGameInfo.generateMessage(isOrigin: Boolean = true): SessionMessage {
        return when (val phase = this.phase) {
            is MafiaPhase.Wait -> MafiaPhaseWaitMessage(
                MafiaPhaseWaitBody(
                    room.id,
                    room.players.map { it.toResponse(room.owner) }.toList(),
                    gameOption.toResponse(),
                ),
            )

            is MafiaPhase.Ready -> MafiaPhaseReadyMessage(
                MafiaPhaseReadyBody(
                    startTime = phase.job.startTime,
                    mafiaGameInfo = generateMafiaGameInfoMessage(
                        roomId = room.id,
                        isRandomMatching = room.isRandomMatching,
                        mafiaPlayer = phase.mafiaPlayer,
                        turnList = phase.turnList,
                        keyword = phase.keyword,
                        gameOption = gameOption,
                    ),
                ),
            )

            is MafiaPhase.Playing -> MafiaPhasePlayingMessage(
                MafiaPhasePlayingBody(
                    round = phase.round,
                    turn = phase.turn,
                    startTurnTime = phase.job.startTime,
                    draw = phase.getDraw(),
                    currentDraw = phase.getCurrentDraw(),
                    mafiaGameInfo = generateMafiaGameInfoMessage(
                        roomId = room.id,
                        isRandomMatching = room.isRandomMatching,
                        mafiaPlayer = phase.mafiaPlayer,
                        turnList = phase.turnList,
                        keyword = phase.keyword,
                        gameOption = gameOption,
                    ),
                ),
            )

            is MafiaPhase.Vote -> MafiaPhaseVoteMessage(
                MafiaPhaseVoteBody(
                    startTime = phase.job.startTime,
                    mafiaGameInfo = if (isOrigin.not()) {
                        generateMafiaGameInfoMessage(
                            roomId = room.id,
                            isRandomMatching = room.isRandomMatching,
                            mafiaPlayer = phase.mafiaPlayer,
                            turnList = phase.turnList,
                            keyword = phase.keyword,
                            gameOption = gameOption,
                        )
                    } else {
                        null
                    },
                    draw = phase.drawData.take(phase.drawData.size).map { it.second },
                    players = phase.players,
                ),
            )

            is MafiaPhase.InferAnswer -> MafiaPhaseInferAnswerMessage(
                MafiaPhaseInferAnswerBody(
                    startTime = phase.job.startTime,
                    mafiaGameInfo = if (isOrigin.not()) {
                        generateMafiaGameInfoMessage(
                            roomId = room.id,
                            isRandomMatching = room.isRandomMatching,
                            mafiaPlayer = phase.mafiaPlayer,
                            turnList = phase.turnList,
                            keyword = phase.keyword,
                            gameOption = gameOption,
                        )
                    } else {
                        null
                    },
                    mafiaAnswer = phase.answer,
                    draw = phase.drawData.take(phase.drawData.size).map { it.second },
                ),
            )

            is MafiaPhase.End -> MafiaPhaseEndMessage(
                MafiaPhaseEndBody(
                    startTime = phase.job.startTime,
                    mafiaGameInfo = if (isOrigin.not()) {
                        generateMafiaGameInfoMessage(
                            roomId = room.id,
                            isRandomMatching = room.isRandomMatching,
                            mafiaPlayer = phase.mafiaPlayer,
                            turnList = phase.turnList,
                            keyword = phase.keyword,
                            gameOption = gameOption,
                        )
                    } else {
                        null
                    },
                    showAnswer = phase.showAnswer,
                    mafiaAnswer = phase.answer,
                    isMafiaWin = phase.isMafiaWin,
                    draw = phase.drawData.take(phase.drawData.size).map { it.second },
                ),
            )
        }
    }

    private fun generateMafiaGameInfoMessage(
        roomId: RoomId,
        isRandomMatching: Boolean,
        mafiaPlayer: MafiaPlayer,
        turnList: List<MafiaPlayer>,
        keyword: MafiaKeyword,
        gameOption: MafiaGameOption,
    ): MafiaGameInfoMessage =
        MafiaGameInfoMessage(
            MafiaGameInfoBody(
                roomId = roomId,
                isRandomMatching = isRandomMatching,
                mafiaUserId = mafiaPlayer.userId,
                turnList = turnList,
                category = keyword.category,
                answer = keyword.answer,
                gameOption = gameOption.toResponse(),
            ),
        )
}
