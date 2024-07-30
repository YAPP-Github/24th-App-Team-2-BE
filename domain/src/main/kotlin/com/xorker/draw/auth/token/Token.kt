package com.xorker.draw.auth.token

import com.xorker.draw.user.UserId

data class Token(
    val accessToken: String,
    val refreshToken: String,
    val userId: UserId,
)
