package com.xorker.draw.mafia

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

internal interface WordJpaRepository : JpaRepository<WordJpaEntity, Long> {

    @Query(
        "select w " +
            "from WordJpaEntity w " +
            "where w.locale = :locale " +
            "order by rand() " +
            "limit 1",
    )
    fun findRandomWord(@Param("locale") locale: String): WordJpaEntity
}
