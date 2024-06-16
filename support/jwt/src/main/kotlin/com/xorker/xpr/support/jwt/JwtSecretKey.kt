package com.xorker.xpr.support.jwt

import io.jsonwebtoken.security.Keys
import java.security.Key

class JwtSecretKey(
    private val secret: String,
) : SignatureKey {
    override val key: Key by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray())
    }
}
