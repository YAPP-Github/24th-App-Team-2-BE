package com.xorker.xpr.oidc.apple

import com.xorker.xpr.exception.UnAuthenticationException
import com.xorker.xpr.oidc.apple.dto.ApplePublicKey
import org.springframework.stereotype.Component

@Component
internal class ApplePublicKeyMatcher {
    internal fun getMatchedApplePublicKey(token: String, keys: List<ApplePublicKey>, header: Map<String, String>): ApplePublicKey {
        val alg = header["alg"] ?: throw UnAuthenticationException
        val kid = header["kid"] ?: throw UnAuthenticationException
        return keys.firstOrNull { key ->
            key.alg == alg && key.kid == kid
        } ?: throw UnAuthenticationException
    }
}
