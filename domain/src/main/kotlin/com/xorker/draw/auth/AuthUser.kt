package com.xorker.draw.auth

data class AuthUser(
    val platformUserId: String,
    val authPlatform: AuthPlatform, // trailing comma
)
