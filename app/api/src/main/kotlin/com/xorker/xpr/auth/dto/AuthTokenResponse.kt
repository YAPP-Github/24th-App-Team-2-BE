package com.xorker.xpr.auth.dto

import com.xorker.xpr.auth.token.Token
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Auth 공통 응답 DTO 입니다.")
data class AuthTokenResponse(
    @Schema(description = "Access Token 입니다.")
    val accessToken: String,
    @Schema(description = "Refresh Token 입니다.")
    val refreshToken: String,
)

fun Token.toResponse(): AuthTokenResponse = AuthTokenResponse(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken,
)
