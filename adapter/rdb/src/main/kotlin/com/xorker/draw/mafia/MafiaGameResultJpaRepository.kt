package com.xorker.draw.mafia

import org.springframework.data.jpa.repository.JpaRepository

internal interface MafiaGameResultJpaRepository : JpaRepository<MafiaGameResultJpaEntity, Long>
