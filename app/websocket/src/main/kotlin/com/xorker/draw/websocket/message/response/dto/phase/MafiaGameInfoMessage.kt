package com.xorker.draw.websocket.message.response.dto.phase

import com.xorker.draw.mafia.MafiaGameOption
import com.xorker.draw.mafia.MafiaPlayer
import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.ResponseAction
import com.xorker.draw.websocket.SessionMessage

data class MafiaGameInfoMessage(
    override val body: MafiaGameInfoBody,
) : SessionMessage {
    override val action: ResponseAction = ResponseAction.GAME_INFO
    override val status: SessionMessage.Status = SessionMessage.Status.OK
}

data class MafiaGameInfoBody(
    val roomId: RoomId,
    val isRandomMatching: Boolean,
    val mafiaUserId: UserId,
    val turnList: List<MafiaPlayer>,
    val category: String,
    val answer: String,
    val gameOption: MafiaGameOptionResponse,
)

data class MafiaGameOptionResponse(
    val minimum: Int,
    val maximum: Int,
    val readyTime: Long,
    val introAnimationTime: Long,
    val roundAnimationTime: Long,
    val round: Int,
    val turnTime: Long,
    val turnCount: Int,
    val voteTime: Long,
    val answerTime: Long,
    val endTime: Long,
)

fun MafiaGameOption.toResponse(): MafiaGameOptionResponse = MafiaGameOptionResponse(
    minimum = minimum,
    maximum = maximum,
    readyTime = readyTime.toMillis(),
    introAnimationTime = introAnimationTime.toMillis(),
    roundAnimationTime = roundAnimationTime.toMillis(),
    round = round,
    turnTime = turnTime.toMillis(),
    turnCount = turnCount,
    voteTime = voteTime.toMillis(),
    answerTime = answerTime.toMillis(),
    endTime = endTime.toMillis(),
)
