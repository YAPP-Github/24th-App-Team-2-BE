package com.yapp.bol.social.google

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.xorker.xpr.exception.OAuthFailureException
import java.security.GeneralSecurityException
import org.springframework.stereotype.Component

@Component
internal class GoogleIdTokenService(
    googleApiProperties: GoogleApiProperties,
) {
    private val idTokenVerifier =
        GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory.getDefaultInstance())
            .setAudience(listOf(googleApiProperties.clientId))
            .build()

    fun getPlatformUserId(token: String): String {
        try {
            val idToken = idTokenVerifier.verify(token)
            return idToken?.payload?.subject ?: throw OAuthFailureException
        } catch (e: GeneralSecurityException) {
            throw OAuthFailureException
        }
    }
}
