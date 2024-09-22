package com.xorker.draw.mafia.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.xorker.draw.mafia.MafiaGameOption
import java.time.Duration

data class RedisMafiaGameOption @JsonCreator constructor(
    @JsonProperty("minimum") val minimum: Int,
    @JsonProperty("maximum") val maximum: Int,
    @JsonProperty("readyTime") val readyTime: Long,
    @JsonProperty("introAnimationTime") val introAnimationTime: Long,
    @JsonProperty("roundAnimationTime") val roundAnimationTime: Long,
    @JsonProperty("round") val round: Int,
    @JsonProperty("turnTime") val turnTime: Long,
    @JsonProperty("turnCount") val turnCount: Int,
    @JsonProperty("voteTime") val voteTime: Long,
    @JsonProperty("answerTime") val answerTime: Long,
    @JsonProperty("endTime") val endTime: Long,
)

fun RedisMafiaGameOption.toGameOption(): MafiaGameOption = MafiaGameOption(
    minimum = this.minimum,
    maximum = this.maximum,
    readyTime = Duration.ofMillis(this.readyTime),
    introAnimationTime = Duration.ofMillis(this.introAnimationTime),
    roundAnimationTime = Duration.ofMillis(this.roundAnimationTime),
    round = this.round,
    turnTime = Duration.ofMillis(this.turnTime),
    turnCount = this.turnCount,
    voteTime = Duration.ofMillis(this.voteTime),
    answerTime = Duration.ofMillis(this.answerTime),
    endTime = Duration.ofMillis(this.endTime),
)
