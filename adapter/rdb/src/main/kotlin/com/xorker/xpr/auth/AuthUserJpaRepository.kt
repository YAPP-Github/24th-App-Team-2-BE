package com.xorker.xpr.auth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

internal interface AuthUserJpaRepository : JpaRepository<AuthUserJpaEntity, Long> {

    @Query(
        "SELECT au FROM AuthUserJpaEntity au " +
            "JOIN FETCH au.user " +
            "WHERE au.platformUserId = :platformUserId " +
            "and au.platform=:platform ",
    )
    fun find(platform: AuthPlatform, platformUserId: String): AuthUserJpaEntity?
}
