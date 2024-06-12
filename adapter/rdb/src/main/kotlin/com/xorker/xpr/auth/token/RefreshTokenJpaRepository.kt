package com.xorker.xpr.auth.token

import org.springframework.data.jpa.repository.JpaRepository

internal interface RefreshTokenJpaRepository : JpaRepository<RefreshTokenJpaEntity, ByteArray> {
    fun deleteByUserId(userId: Long)
}
