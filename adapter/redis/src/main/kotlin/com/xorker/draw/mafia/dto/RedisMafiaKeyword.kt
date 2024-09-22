package com.xorker.draw.mafia.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.xorker.draw.mafia.MafiaKeyword

data class RedisMafiaKeyword @JsonCreator constructor(
    @JsonProperty("category") val category: String,
    @JsonProperty("answer") val answer: String,
)

fun RedisMafiaKeyword.toMafiaKeyword(): MafiaKeyword = MafiaKeyword(
    answer = this.answer,
    category = this.category,
)
