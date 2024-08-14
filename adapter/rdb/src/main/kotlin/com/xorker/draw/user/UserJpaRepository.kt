package com.xorker.draw.user

import org.springframework.data.jpa.repository.JpaRepository

internal interface UserJpaRepository : JpaRepository<UserJpaEntity, Long>
