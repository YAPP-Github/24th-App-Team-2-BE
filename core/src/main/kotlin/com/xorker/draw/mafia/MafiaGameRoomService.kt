package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.room.Room
import com.xorker.draw.room.RoomId
import com.xorker.draw.room.RoomRepository
import com.xorker.draw.user.User
import org.slf4j.MDC
import org.springframework.stereotype.Service

@Service
internal class MafiaGameRoomService(
    private val mafiaGameRepository: MafiaGameRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
    private val mafiaPhaseMessenger: MafiaPhaseMessenger,
    private val roomRepository: RoomRepository,
) : UserConnectionUseCase {

    override fun connectUser(user: User, roomId: RoomId?, locale: String) {
        val roomIdNotNull = roomId ?: generateRoomId()

        MDC.put("roomId", roomIdNotNull.value)

        val gameInfo = connectGame(user, roomIdNotNull, locale, false)

        mafiaPhaseMessenger.unicastPhase(user.id, gameInfo)
        mafiaGameMessenger.broadcastPlayerList(gameInfo)
    }

    fun connectGame(user: User, roomId: RoomId, locale: String, isRandomMatching: Boolean): MafiaGameInfo {
        var gameInfo = mafiaGameRepository.getGameInfo(roomId)

        if (gameInfo == null) {
            val player = MafiaPlayer(user.id, user.name, generateColor(null))
            gameInfo = createGameInfo(roomId, locale, player, isRandomMatching)
        } else {
            val player = gameInfo.findPlayer(user.id)
            if (player != null) {
                player.connect()
            } else {
                gameInfo.room.add(MafiaPlayer(user.id, user.name, generateColor(gameInfo)))
            }
        }

        mafiaGameRepository.saveGameInfo(gameInfo)
        return gameInfo
    }

    override fun disconnectUser(user: User) {
        val userId = user.id
        val gameInfo = mafiaGameRepository.getGameInfo(userId) ?: return

        if (gameInfo.phase == MafiaPhase.Wait) {
            exitUser(user)
            return
        }

        val player = gameInfo.findPlayer(userId) ?: return

        player.disconnect()

        if (gameInfo.room.players.all { it.isConnect().not() }) {
            mafiaGameRepository.removeGameInfo(gameInfo)
        } else {
            mafiaGameRepository.saveGameInfo(gameInfo)
            mafiaGameMessenger.broadcastPlayerList(gameInfo)
        }
    }

    override fun exitUser(user: User) {
        val userId = user.id
        val gameInfo = mafiaGameRepository.getGameInfo(userId) ?: return

        if (gameInfo.phase != MafiaPhase.Wait) {
            disconnectUser(user)
            return
        }

        val player = gameInfo.findPlayer(userId) ?: return

        gameInfo.room.remove(player)

        if (gameInfo.room.players.isEmpty()) {
            mafiaGameRepository.removeGameInfo(gameInfo)
            return
        }

        if (gameInfo.room.owner == player) {
            gameInfo.room.owner = gameInfo.room.players.first()
        }
        mafiaGameRepository.removePlayer(player.userId)
        mafiaGameRepository.saveGameInfo(gameInfo)
        mafiaGameMessenger.broadcastPlayerList(gameInfo)
    }

    private fun generateColor(gameInfo: MafiaGameInfo?): String {
        val alreadyUsedColors = gameInfo?.room?.players?.map { it.color }?.toList() ?: emptyList()

        return COLOR_LIST
            .filterNot { alreadyUsedColors.contains(it) }
            .shuffled()
            .first()
    }

    private fun createGameInfo(roomId: RoomId, locale: String, player: MafiaPlayer, isRandomMatching: Boolean): MafiaGameInfo {
        val room = createRoom(roomId, locale, player, isRandomMatching)
        return MafiaGameInfo(
            room = room,
            phase = MafiaPhase.Wait,
            gameOption = MafiaGameOption(),
        )
    }

    private fun createRoom(roomId: RoomId, locale: String, player: MafiaPlayer, isRandomMatching: Boolean): Room<MafiaPlayer> {
        val language = locale.lowercase()
        if (language !in languages) {
            throw InvalidRequestValueException
        }

        val room = Room(
            id = roomId,
            locale = language,
            owner = player,
            maxMemberNum = 10,
            isRandomMatching = isRandomMatching,
        )
        return room
    }

    fun generateRoomId(): RoomId {
        var value: String

        do {
            val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
            value = (1..6)
                .map { charset.random() }
                .joinToString("")
        } while (roomRepository.getRoom(RoomId(value)) != null)

        return RoomId(value)
    }

    companion object {
        private val COLOR_LIST = mutableListOf(
            "FF5D47",
            "FF9736",
            "FFD536",
            "ADE639",
            "3CB371",
            "6AB0F1",
            "4B78EC",
            "A162F1",
            "FD66C1",
            "7E91A6",
        )

        private val languages = mutableSetOf("ko", "en")
    }
}
