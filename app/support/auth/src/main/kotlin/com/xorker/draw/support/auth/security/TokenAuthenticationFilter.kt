package com.xorker.draw.support.auth.security

import com.xorker.draw.support.auth.core.TokenUseCase
import com.xorker.draw.user.UserId
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
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
            tokenUseCase.getUserId(accessToken)?.let {
                setAuthentication(request, accessToken, it)
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun getAccessToken(request: HttpServletRequest): String? {
        val accessToken = request.getHeader(HEADER_AUTHORIZATION)
        if (accessToken.isNullOrBlank().not() && accessToken.startsWith(HEADER_BEARER)) {
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
