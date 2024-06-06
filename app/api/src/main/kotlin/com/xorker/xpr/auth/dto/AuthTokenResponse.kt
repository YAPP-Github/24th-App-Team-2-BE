package com.xorker.xpr.auth.dto

import com.xorker.xpr.auth.token.Token

data class AuthTokenResponse(
    val accessToken: String,
    val refreshToken: String,
)

fun Token.toResponse(): AuthTokenResponse = AuthTokenResponse(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken
)
