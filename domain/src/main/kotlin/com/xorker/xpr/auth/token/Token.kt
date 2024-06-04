package com.xorker.xpr.auth.token

data class Token(
    val accessToken: String,
    val refreshToken: String,
)
