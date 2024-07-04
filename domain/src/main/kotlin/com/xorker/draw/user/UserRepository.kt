package com.xorker.draw.user

import com.xorker.draw.auth.AuthPlatform

interface UserRepository {
    fun getUser(platform: AuthPlatform, platformUserId: String): User?

    fun getUser(userId: UserId): User?

    fun createUser(platform: AuthPlatform, platformUserId: String, userName: String): User

    fun createUser(userName: String): User

    fun withdrawal(userId: UserId)
}
