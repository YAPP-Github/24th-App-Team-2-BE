package com.xorker.xpr.auth

interface AuthRepository {
    fun getPlatformUserId(authType: AuthType, token: String): String
}