package com.xorker.xpr.auth

import com.xorker.xpr.auth.token.AccessTokenRepository
import com.xorker.xpr.auth.token.RefreshTokenRepository
import com.xorker.xpr.auth.token.Token
import com.xorker.xpr.user.UserId
import com.xorker.xpr.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val accessTokenRepository: AccessTokenRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
) : AuthUseCase {

    override fun signIn(authType: AuthType, token: String): Token {
        val platformUserId = authRepository.getPlatformUserId(authType, token)
        val user = userRepository.getOrCreateUser(authType.authPlatform, platformUserId)
        return createToken(user.id)
    }

    override fun reissue(refreshToken: String): Token {
        val userId = refreshTokenRepository.getUserIdOrThrow(refreshToken)
        return createToken(userId)
    }

    @Transactional
    override fun withdrawal(userId: UserId) {
        refreshTokenRepository.deleteRefreshToken(userId)
        userRepository.withdrawal(userId)
    }

    private fun createToken(userId: UserId): Token {
        return Token(
            accessTokenRepository.createAccessToken(userId),
            refreshTokenRepository.createRefreshToken(userId),
        )
    }
}