package com.xorker.draw.mafia.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.xorker.draw.mafia.MafiaPlayer
import com.xorker.draw.user.UserId

data class RedisMafiaPlayer @JsonCreator constructor(
    @JsonProperty("id") val id: Long,
    @JsonProperty("nickname") val nickname: String,
    @JsonProperty("color") val color: String,
    @JsonProperty("connect") val isConnect: Boolean,
)

fun RedisMafiaPlayer.toPlayer(): MafiaPlayer {
    val mafiaPlayer = MafiaPlayer(
        userId = UserId(this.id),
        nickname = this.nickname,
        color = this.color,
    )

    if (this.isConnect.not()) {
        mafiaPlayer.disconnect()
    }

    return mafiaPlayer
}
