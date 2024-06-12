package com.xorker.xpr.oauth

import com.xorker.xpr.auth.AuthRepository
import com.xorker.xpr.auth.AuthType
import com.xorker.xpr.oauth.apple.AppleAuthService
import org.springframework.stereotype.Component

@Component
internal class OAuthAdapter(
    private val appleAuthService: AppleAuthService,
) : AuthRepository {
    override fun getPlatformUserId(authType: AuthType, token: String): String {
        return when (authType) {
            AuthType.APPLE_ID_TOKEN -> appleAuthService.getPlatformUserId(token)
        }
    }
}
