package com.xorker.xpr.auth

enum class AuthType(val authPlatform: AuthPlatform) {
    GOOGLE_ID_TOKEN(AuthPlatform.GOOGLE),
    APPLE_ID_TOKEN(AuthPlatform.APPLE),
}
