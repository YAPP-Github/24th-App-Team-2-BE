package com.xorker.xpr.oauth.apple.dto

internal data class ApplePublicKeys(
    val keys: List<ApplePublicKey>,
) {
    operator fun get(alg: String, kid: String): ApplePublicKey? {
        return keys.firstOrNull { key ->
            key.alg == alg && key.kid == kid
        }
    }
}
