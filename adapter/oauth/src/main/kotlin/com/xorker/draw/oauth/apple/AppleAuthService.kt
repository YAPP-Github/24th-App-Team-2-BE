package com.xorker.draw.oauth.apple

import org.springframework.stereotype.Component

@Component
internal class AppleAuthService(
    private val keyGenerator: ApplePublicKeyGenerator,
    private val validator: AppleIdTokenValidator,
) {
    internal fun getPlatformUserId(token: String): String {
        val publicKey = keyGenerator.generatePublicKey(token)

        return validator.validateAndGetSubject(token, publicKey)
    }
}
