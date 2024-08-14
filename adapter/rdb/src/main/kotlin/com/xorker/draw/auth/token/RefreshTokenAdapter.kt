package com.xorker.draw.auth.token

import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.user.UserId
import java.nio.ByteBuffer
import java.security.SecureRandom
import java.time.LocalDateTime
import java.util.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
internal class RefreshTokenAdapter(
    private val jpaRepository: RefreshTokenJpaRepository,
) : RefreshTokenRepository {
    private val random: SecureRandom by lazy {
        val buffer = ByteBuffer.allocate(Long.SIZE_BYTES)
        buffer.putLong(System.currentTimeMillis())
        SecureRandom(buffer.array())
    }

    override fun getUserIdOrThrow(refreshToken: String): UserId {
        val refreshTokenEntity = jpaRepository.findByIdOrNull(refreshToken.toBinary()) ?: throw InvalidRequestValueException

        val now = LocalDateTime.now()
        val expiredAt = refreshTokenEntity.expiredAt

        if (expiredAt.isBefore(now)) {
            throw InvalidRequestValueException
        }

        return UserId(refreshTokenEntity.userId)
    }

    override fun createRefreshToken(userId: UserId): String {
        val token = ByteArray(TOKEN_BYTE_SIZE).apply {
            random.nextBytes(this)
        }
        val expiredAt = LocalDateTime.now().plusYears(100) // TODO 소셜 & 익명 구분 필요

        jpaRepository.save(
            RefreshTokenJpaEntity.of(token, userId, expiredAt),
        )

        return Base64.getEncoder().encodeToString(token)
    }

    override fun deleteRefreshToken(userId: UserId) {
        jpaRepository.deleteByUserId(userId.value)
    }

    private fun String.toBinary(): ByteArray {
        return Base64.getDecoder().decode(this)
    }

    companion object {
        private const val TOKEN_BYTE_SIZE = 60 * 6 / 8 // 45 Bytes
    }
}
