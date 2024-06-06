package com.xorker.xpr.auth.dto

data class AuthReissueResponse(
    val accessToken: String,
    val refreshToken: String,
)