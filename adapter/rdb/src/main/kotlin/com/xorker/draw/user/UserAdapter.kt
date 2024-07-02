package com.xorker.draw.user

import com.xorker.draw.auth.AuthPlatform
import com.xorker.draw.auth.AuthUserJpaEntity
import com.xorker.draw.auth.AuthUserJpaRepository
import com.xorker.draw.exception.NotFoundUserException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
internal class UserAdapter(
    private val userJpaRepository: UserJpaRepository,
    private val authUserJpaRepository: AuthUserJpaRepository,
) : UserRepository {
    override fun getUser(platform: AuthPlatform, platformUserId: String): User? =
        authUserJpaRepository.find(platform, platformUserId)?.user?.toDomain()

    override fun getUser(userId: UserId): User? =
        userJpaRepository.findByIdOrNull(userId.value)?.toDomain()

    override fun createUser(platform: AuthPlatform, platformUserId: String, userName: String): User {
        val user = UserJpaEntity()
        val authUser = authUserJpaRepository.save(AuthUserJpaEntity.of(platform, platformUserId, user))
        return authUser.user.toDomain()
    }

    override fun createUser(userName: String): User {
        val user = UserJpaEntity.from(userName)
        val savedUser = userJpaRepository.save(user)
        return savedUser.toDomain()
    }

    @Transactional
    override fun withdrawal(userId: UserId) {
        val user = userJpaRepository.findByIdOrNull(userId.value) ?: throw NotFoundUserException
        user.withdrawal()
        userJpaRepository.save(user)
    }
}
