package com.xorker.xpr.support.auth.security

import com.xorker.xpr.support.auth.core.TokenUseCase
import com.xorker.xpr.user.UserId
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

internal class TokenAuthenticationFilter(
    private val tokenUseCase: TokenUseCase,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val accessToken = getAccessToken(request)
        if (accessToken != null) {
            val userId = tokenUseCase.getUserId(accessToken)
            setAuthentication(request, accessToken, userId)
        }
        filterChain.doFilter(request, response)
    }

    private fun getAccessToken(request: HttpServletRequest): String? {
        val accessToken = request.getHeader(HEADER_AUTHORIZATION)
        if (accessToken != null && accessToken.startsWith(HEADER_BEARER)) {
            return accessToken.substring(HEADER_BEARER.length)
        }
        return null
    }

    private fun setAuthentication(request: HttpServletRequest, token: String, userId: UserId) {
        val details = WebAuthenticationDetailsSource().buildDetails(request)
        val authentication = TokenAuthentication(token, userId, details)

        val securityContext = SecurityContextHolder.getContext()
        securityContext.authentication = authentication
    }

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val HEADER_BEARER = "bearer "
    }
}
