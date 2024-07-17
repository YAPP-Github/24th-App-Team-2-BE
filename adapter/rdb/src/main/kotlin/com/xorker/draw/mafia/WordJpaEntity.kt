package com.xorker.draw.mafia

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "word")
internal class WordJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    var id: Long = 0

    @Column(name = "locale")
    lateinit var locale: String
        protected set

    @Column(name = "category")
    lateinit var category: String
        protected set

    @Column(name = "keyword")
    lateinit var keyword: String
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_word_id")
    var word: WordJpaEntity? = null

    @OneToMany(mappedBy = "word")
    val words = mutableListOf<WordJpaEntity>()
}

internal fun WordJpaEntity.toDomain(): MafiaKeyword = MafiaKeyword(
    category = category,
    answer = keyword,
)
