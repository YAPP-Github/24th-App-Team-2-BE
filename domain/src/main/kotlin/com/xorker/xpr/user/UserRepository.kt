package com.xorker.xpr.user

import com.xorker.xpr.auth.AuthPlatform

interface UserRepository {
    fun getOrCreateUser(platform: AuthPlatform, platformUserId: String): User

    fun getUser(userId: UserId): User?

    fun withdrawal(userId: UserId)
}
