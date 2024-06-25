package com.xorker.xpr.auth

import com.xorker.xpr.auth.token.AccessTokenCommandRepository
import com.xorker.xpr.auth.token.RefreshTokenRepository
import com.xorker.xpr.auth.token.Token
import com.xorker.xpr.user.User
import com.xorker.xpr.user.UserId
import com.xorker.xpr.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class AuthService(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val accessTokenCommandRepository: AccessTokenCommandRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
) : AuthUseCase {
    @Transactional
    override fun signIn(authType: AuthType, token: String): Token {
        val platformUserId = authRepository.getPlatformUserId(authType, token)
        val user = userRepository.getUser(authType.authPlatform, platformUserId) ?: createUser(authType, platformUserId)

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

    private fun createUser(authType: AuthType, platformUserId: String): User {
        val userName = authRepository.getPlatformUserName(authType, platformUserId)

        return userRepository.createUser(authType.authPlatform, platformUserId, userName)
    }

    private fun createToken(userId: UserId): Token {
        return Token(
            accessTokenCommandRepository.createAccessToken(userId),
            refreshTokenRepository.createRefreshToken(userId),
        )
    }
}
