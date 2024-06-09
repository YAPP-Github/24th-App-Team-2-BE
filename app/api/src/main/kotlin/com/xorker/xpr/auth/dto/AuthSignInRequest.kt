package com.xorker.xpr.auth.dto

import com.xorker.xpr.auth.AuthType

data class AuthSignInRequest(
    val authType: AuthType,
    val token: String,
)
