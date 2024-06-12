package com.xorker.xpr.user

import org.springframework.data.jpa.repository.JpaRepository

internal interface UserJpaRepository : JpaRepository<UserJpaEntity, Long>
