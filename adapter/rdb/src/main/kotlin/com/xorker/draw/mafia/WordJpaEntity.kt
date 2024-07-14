package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidGameStatusException
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "word")
internal class WordJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    var id: Long = 0

    @Column(name = "category_ko")
    lateinit var categoryKo: String
        protected set

    @Column(name = "category_en")
    lateinit var categoryEn: String
        protected set

    @Column(name = "keyword_ko")
    lateinit var keywordKo: String
        protected set

    @Column(name = "keyword_en")
    lateinit var keywordEn: String
        protected set
}

internal fun WordJpaEntity.toDomain(locale: Locale): MafiaKeyword = when (locale.language) {
    Locale.KOREAN.language -> {
        MafiaKeyword(
            category = this.categoryKo,
            answer = this.keywordKo,
        )
    }

    Locale.US.language -> {
        MafiaKeyword(
            category = this.categoryEn,
            answer = this.keywordEn,
        )
    }

    else -> {
        throw InvalidGameStatusException
    }
}
