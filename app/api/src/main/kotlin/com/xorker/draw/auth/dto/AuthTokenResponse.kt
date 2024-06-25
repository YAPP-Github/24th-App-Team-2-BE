package com.xorker.draw.auth.dto

import com.xorker.draw.auth.token.Token

data class AuthTokenResponse(
    val accessToken: String,
    val refreshToken: String,
)

fun Token.toResponse(): AuthTokenResponse = AuthTokenResponse(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken,
)
