package com.xorker.xpr.support.auth.security

import com.xorker.xpr.user.UserId
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.WebAuthenticationDetails

class TokenAuthentication(
    private val token: String,
    private val userId: UserId,
    private val details: WebAuthenticationDetails,
) : Authentication {
    private var isAuthenticated = true

    override fun getName(): String = userId.toString()

    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()

    override fun getCredentials(): String = token

    override fun getDetails(): WebAuthenticationDetails = details

    override fun getPrincipal(): UserId = userId

    override fun isAuthenticated(): Boolean = isAuthenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.isAuthenticated = isAuthenticated
    }
}
