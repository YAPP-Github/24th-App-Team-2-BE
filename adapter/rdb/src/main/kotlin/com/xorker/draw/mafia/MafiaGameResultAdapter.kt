package com.xorker.draw.mafia

import com.fasterxml.jackson.databind.ObjectMapper
import com.xorker.draw.exception.NotFoundWordException
import org.springframework.stereotype.Component

@Component
internal class MafiaGameResultAdapter(
    private val objectMapper: ObjectMapper,
    private val mafiaGameResultJpaRepository: MafiaGameResultJpaRepository,
    private val wordJpaRepository: WordJpaRepository,
) : MafiaGameResultRepository {

    override fun saveMafiaGameResult(gameInfo: MafiaGameInfo) {
        val phase = gameInfo.phase
        assertIs<MafiaPhase.End>(phase)

        val keyword = phase.keyword
        val findEntity = wordJpaRepository.findByKeyword(keyword.answer) ?: throw NotFoundWordException

        val mafiaGameResult = gameInfo.toMafiaGameResult()

        val serializedMafiaGameResult = objectMapper.writeValueAsString(mafiaGameResult)

        val entity = MafiaGameResultJpaEntity.of(serializedMafiaGameResult, findEntity.id)

        mafiaGameResultJpaRepository.save(entity)
    }
}
