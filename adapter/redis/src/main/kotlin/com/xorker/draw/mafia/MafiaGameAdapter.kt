package com.xorker.draw.mafia

import com.xorker.draw.mafia.dto.RedisMafiaGameInfo
import com.xorker.draw.mafia.dto.toRedisMafiaGameInfo
import com.xorker.draw.room.Room
import com.xorker.draw.room.RoomId
import com.xorker.draw.room.RoomRepository
import com.xorker.draw.support.metric.MetricManager
import com.xorker.draw.timer.TimerRepository
import com.xorker.draw.user.UserId
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
internal class MafiaGameAdapter(
    private val metricManager: MetricManager,
    private val redisTemplateWithObject: RedisTemplate<String, RedisMafiaGameInfo>,
    private val redisTemplate: RedisTemplate<String, String>,
    private val timerRepository: TimerRepository,
) : MafiaGameRepository, RoomRepository {

    override fun saveGameInfo(gameInfo: MafiaGameInfo) {
        val room = gameInfo.room
        if (room.isEmpty()) {
            removeGameInfo(gameInfo)
        } else {
            val findGameInfo = redisTemplateWithObject
                .opsForValue()
                .get(room.id.value)

            if (findGameInfo == null) {
                metricManager.increaseGameCount()
            }

            redisTemplateWithObject
                .opsForValue()
                .set(room.id.value, gameInfo.toRedisMafiaGameInfo())

            room.players.forEach {
                redisTemplate
                    .opsForValue()
                    .set(it.userId.value.toString(), room.id.value)
            }
        }
    }

    override fun removeGameInfo(gameInfo: MafiaGameInfo) {
        metricManager.decreaseGameCount()

        val room = gameInfo.room

        room.players
            .map { it.userId }
            .forEach { removePlayer(it) }

        val phase = gameInfo.phase

        if (phase is MafiaPhaseWithTimer) {
            timerRepository.cancelTimer(room.id)
        }

        redisTemplateWithObject.delete(room.id.value)
    }

    override fun getGameInfo(roomId: RoomId): MafiaGameInfo? {
        return redisTemplateWithObject
            .opsForValue()
            .get(roomId.value)
            ?.toMafiaGameInfo()
    }

    override fun getGameInfo(userId: UserId): MafiaGameInfo? {
        val roomId = redisTemplate
            .opsForValue()
            .get(userId.value.toString()) ?: return null

        return redisTemplateWithObject
            .opsForValue()
            .get(roomId)
            ?.toMafiaGameInfo()
    }

    override fun removePlayer(userId: UserId) {
        redisTemplate.delete(userId.value.toString())
    }

    override fun getRoom(roomId: RoomId): Room<MafiaPlayer>? {
        return redisTemplateWithObject
            .opsForValue()
            .get(roomId.value)
            ?.toMafiaGameInfo()
            ?.room
    }
}
