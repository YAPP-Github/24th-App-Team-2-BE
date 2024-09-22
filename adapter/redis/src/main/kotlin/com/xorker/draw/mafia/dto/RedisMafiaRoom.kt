package com.xorker.draw.mafia.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.xorker.draw.mafia.MafiaPlayer
import com.xorker.draw.room.Room
import com.xorker.draw.room.RoomId

data class RedisMafiaRoom @JsonCreator constructor(
    @JsonProperty("id") val id: String,
    @JsonProperty("locale") val locale: String,
    @JsonProperty("owner") val owner: RedisMafiaPlayer,
    @JsonProperty("maxMemberNum") val maxMemberNum: Int,
    @JsonProperty("players") val players: List<RedisMafiaPlayer>,
    @JsonProperty("randomMatching") val isRandomMatching: Boolean,
)

fun RedisMafiaRoom.toRoom(): Room<MafiaPlayer> = Room(
    id = RoomId(this.id),
    locale = this.locale,
    owner = this.owner.toPlayer(),
    maxMemberNum = this.maxMemberNum,
    players = this.players.map { player ->
        player.toPlayer()
    }.toMutableList(),
    isRandomMatching = isRandomMatching,
)
