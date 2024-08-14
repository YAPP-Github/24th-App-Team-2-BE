package com.xorker.draw.auth

import com.xorker.draw.auth.token.AccessTokenRepository
import com.xorker.draw.auth.token.RefreshTokenRepository
import com.xorker.draw.auth.token.Token
import com.xorker.draw.user.User
import com.xorker.draw.user.UserId
import com.xorker.draw.user.UserRepository
import java.time.Duration
import java.time.Period
import java.time.temporal.TemporalAmount
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class AuthService(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val accessTokenRepository: AccessTokenRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
) : AuthUseCase {
    @Transactional
    override fun signIn(authType: AuthType, token: String): Token {
        val platformUserId = authRepository.getPlatformUserId(authType, token)
        val user = userRepository.getUser(authType.authPlatform, platformUserId) ?: createUser(authType, platformUserId)

        return createToken(user.id, Duration.ofHours(3))
    }

    override fun anonymousSignIn(): Token {
        val user = userRepository.createUser(""); // TODO 이름 정책 정해지면 변경 예정

        return createToken(user.id, Period.ofYears(100))
    }

    override fun reissue(refreshToken: String): Token {
        val userId = refreshTokenRepository.getUserIdOrThrow(refreshToken)

        return createToken(userId, Period.ofYears(100))
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

    private fun createToken(userId: UserId, expiredTime: TemporalAmount): Token {
        return Token(
            accessToken = accessTokenRepository.createAccessToken(userId, expiredTime),
            refreshToken = refreshTokenRepository.createRefreshToken(userId),
            userId = userId,
        )
    }
}
