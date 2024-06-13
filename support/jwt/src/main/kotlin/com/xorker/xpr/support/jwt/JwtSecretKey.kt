package com.xorker.xpr.support.jwt

import io.jsonwebtoken.security.Keys
import java.security.Key
import org.springframework.beans.factory.annotation.Value

object JwtSecretKey : SignatureKey {
    @Value("\${jwt.secret}")
    private lateinit var secret: String
    private var secretKey: Key? = null

    override fun getKey(): Key {
        if (secretKey == null) {
            secretKey = Keys.hmacShaKeyFor(secret.toByteArray())
        }
        return secretKey!!
    }
}
