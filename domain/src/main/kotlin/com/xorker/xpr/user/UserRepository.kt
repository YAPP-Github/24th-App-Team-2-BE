package com.xorker.xpr.user

import com.xorker.xpr.auth.AuthPlatform

interface UserRepository {
    fun getUser(platform: AuthPlatform, platformUserId: String): User?

    fun createUser(platform: AuthPlatform, platformUserId: String, userName: String): User

    fun getUser(userId: UserId): User?

    fun withdrawal(userId: UserId)
}
