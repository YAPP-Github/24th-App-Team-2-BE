package com.xorker.draw.auth.dto

import com.xorker.draw.auth.AuthType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "소셜 로그인/회원가입 요청 DTO 입니다.")
data class AuthSignInRequest(
    @Schema(description = "AuthType -> { APPLE_ID_TOKEN, GOOGLE_ID_TOKEN } 플랫폼과 일치하는 값을 넣어주시면 됩니다.")
    val authType: AuthType,
    @Schema(description = "플랫폼 ID Token 넣어주시면 됩니다.")
    val token: String,
)
