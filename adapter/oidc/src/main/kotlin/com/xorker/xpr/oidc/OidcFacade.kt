package com.xorker.xpr.oidc

import com.xorker.xpr.auth.AuthRepository
import com.xorker.xpr.auth.AuthType
import com.xorker.xpr.oidc.apple.AppleOidcHandler
import org.springframework.stereotype.Component

@Component
internal class OidcFacade(
    private val appleOidcHandler: AppleOidcHandler,
) : AuthRepository {
    override fun getPlatformUserId(authType: AuthType, token: String): String {
        return when (authType) {
            AuthType.APPLE_ID_TOKEN -> appleOidcHandler.getPlatformUserId(token)
        }
    }
}
