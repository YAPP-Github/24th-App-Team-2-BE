package com.xorker.draw.mafia

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

internal interface WordJpaRepository : JpaRepository<WordJpaEntity, Long> {

    @Query("select w from WordJpaEntity w order by rand() limit 1")
    fun findRandomWord(): WordJpaEntity?
}
