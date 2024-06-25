package com.xorker.xpr.auth.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "AT/RT 재발급 요청 DTO 입니다.")
data class AuthReissueRequest(
    @Schema(description = "Refresh Token 넣어주시면 됩니다.")
    val refreshToken: String,
)
