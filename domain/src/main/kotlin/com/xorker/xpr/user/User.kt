package com.xorker.xpr.user

@JvmInline
value class UserId(val value: Long)

data class User(
    val id: UserId,
)