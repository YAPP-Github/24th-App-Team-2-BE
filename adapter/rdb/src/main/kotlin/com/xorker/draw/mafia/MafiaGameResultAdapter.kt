package com.xorker.draw.mafia

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
internal class MafiaGameResultAdapter(
    private val objectMapper: ObjectMapper,
    private val mafiaGameResultJpaRepository: MafiaGameResultJpaRepository,
) : MafiaGameResultRepository {
    override fun saveMafiaGameResult(gameInfo: MafiaGameInfo) {
        val mafiaGameResult = gameInfo.toMafiaGameResult()
        val serializedMafiaGameResult = objectMapper.writeValueAsString(mafiaGameResult)

        val entity = MafiaGameResultJpaEntity.of(serializedMafiaGameResult)

        mafiaGameResultJpaRepository.save(entity)
    }
}
