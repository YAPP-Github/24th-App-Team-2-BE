package com.xorker.draw.auth.token

data class Token(
    val accessToken: String,
    val refreshToken: String,
)
