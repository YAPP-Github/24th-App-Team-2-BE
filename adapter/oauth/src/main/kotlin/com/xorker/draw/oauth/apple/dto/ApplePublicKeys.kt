package com.xorker.draw.oauth.apple.dto

internal data class ApplePublicKeys(
    val keys: List<ApplePublicKey>,
) {
    operator fun get(alg: String, kid: String): ApplePublicKey? {
        return keys.firstOrNull { key ->
            key.alg == alg && key.kid == kid
        }
    }
}
