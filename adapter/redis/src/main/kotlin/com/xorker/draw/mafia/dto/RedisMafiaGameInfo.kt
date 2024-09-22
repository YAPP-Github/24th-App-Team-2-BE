package com.xorker.draw.mafia.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.xorker.draw.mafia.MafiaGameInfo

data class RedisMafiaGameInfo @JsonCreator constructor(
    @JsonProperty("room") val room: RedisRoom,
    @JsonProperty("phase") val phase: RedisMafiaPhase,
    @JsonProperty("gameOption") val gameOption: RedisGameOption,
) {
    fun toMafiaGameInfo(): MafiaGameInfo = MafiaGameInfo(
        room = room.toRoom(),
        phase = phase.toMafiaPhase(),
        gameOption = gameOption.toGameOption(),
    )
}

fun MafiaGameInfo.toRedisMafiaGameInfo(): RedisMafiaGameInfo = RedisMafiaGameInfo(
    room = RedisRoom(
        id = room.id.value,
        locale = room.locale,
        owner = RedisMafiaPlayer(
            id = room.owner.userId.value,
            nickname = room.owner.nickname,
            color = room.owner.color,
            isConnect = room.owner.isConnect(),
        ),
        maxMemberNum = room.maxMemberNum,
        players = room.players.map { player ->
            RedisMafiaPlayer(
                id = player.userId.value,
                nickname = player.nickname,
                color = player.color,
                isConnect = player.isConnect(),
            )
        },
        isRandomMatching = room.isRandomMatching,
    ),
    phase = phase.toRedisMafiaPhase(),
    gameOption = RedisGameOption(
        minimum = gameOption.minimum,
        maximum = gameOption.maximum,
        readyTime = gameOption.readyTime.toMillis(),
        introAnimationTime = gameOption.introAnimationTime.toMillis(),
        roundAnimationTime = gameOption.roundAnimationTime.toMillis(),
        round = gameOption.round,
        turnTime = gameOption.turnTime.toMillis(),
        turnCount = gameOption.turnCount,
        voteTime = gameOption.voteTime.toMillis(),
        answerTime = gameOption.answerTime.toMillis(),
        endTime = gameOption.endTime.toMillis(),
    ),
)
