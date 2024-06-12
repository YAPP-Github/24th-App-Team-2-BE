package com.xorker.xpr.user

import com.xorker.xpr.auth.AuthPlatform
import com.xorker.xpr.exception.NotFoundUserException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class UserAdapter(
    private val jpaRepository: UserJpaRepository,
): UserRepository {
    override fun getOrCreateUser(platform: AuthPlatform, platformUserId: String): User {
        TODO("Not yet implemented")
    }

    override fun getUser(userId: UserId): User? =
        jpaRepository.findByIdOrNull(userId.value)?.toDomain()

    @Transactional
    override fun withdrawal(userId: UserId) {
        val user = jpaRepository.findByIdOrNull(userId.value) ?: throw NotFoundUserException
        user.withdrawal()
        jpaRepository.save(user)
    }
}

