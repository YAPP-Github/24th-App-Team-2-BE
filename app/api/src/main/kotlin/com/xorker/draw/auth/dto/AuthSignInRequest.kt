package com.xorker.draw.auth.dto

import com.xorker.draw.auth.AuthType

data class AuthSignInRequest(
    val authType: AuthType,
    val token: String,
)
