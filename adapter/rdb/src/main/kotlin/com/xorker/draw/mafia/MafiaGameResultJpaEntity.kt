package com.xorker.draw.mafia

import com.xorker.draw.BaseJpaEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "mafia_game_result")
internal class MafiaGameResultJpaEntity : BaseJpaEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mafia_game_result_id")
    var id: Long = 0

    @Column(name = "game_result", columnDefinition = "TEXT")
    lateinit var gameResult: String
        protected set

    @Column(name = "word_id", columnDefinition = "bigint")
    var wordId: Long = 0

    companion object {
        internal fun of(gameResult: String, wordId: Long): MafiaGameResultJpaEntity {
            return MafiaGameResultJpaEntity().apply {
                this.gameResult = gameResult
                this.wordId = wordId
            }
        }
    }
}
