package com.xorker.draw.oauth

import com.xorker.draw.auth.AuthRepository
import com.xorker.draw.auth.AuthType
import com.xorker.draw.oauth.apple.AppleAuthService
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

    override fun getPlatformUserName(authType: AuthType, platformUserId: String): String {
        return ""
    }
}
