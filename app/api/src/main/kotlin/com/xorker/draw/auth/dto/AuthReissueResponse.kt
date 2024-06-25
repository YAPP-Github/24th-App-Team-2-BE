package com.xorker.draw.auth.dto

data class AuthReissueResponse(
    val accessToken: String,
    val refreshToken: String,
)
