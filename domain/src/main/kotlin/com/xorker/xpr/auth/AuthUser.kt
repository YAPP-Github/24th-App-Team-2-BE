package com.xorker.xpr.auth

data class AuthUser(
    val platformUserId: String,
    val authPlatform: AuthPlatform, // trailing comma
)
