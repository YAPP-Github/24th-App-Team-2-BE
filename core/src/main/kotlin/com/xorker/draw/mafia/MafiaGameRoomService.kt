package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.room.Room
import com.xorker.draw.room.RoomId
import com.xorker.draw.user.User
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.SessionEventListener
import org.springframework.stereotype.Service

@Service
internal class MafiaGameRoomService(
    private val mafiaGameRepository: MafiaGameRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
    private val mafiaPhaseMessenger: MafiaPhaseMessenger,
) : SessionEventListener {

    override fun connectSession(userId: UserId, roomId: RoomId, nickname: String, locale: String) {
        var gameInfo = mafiaGameRepository.getGameInfo(roomId)

        if (gameInfo == null) {
            val player = MafiaPlayer(userId, nickname, generateColor(null))
            gameInfo = createGameInfo(roomId, locale, player)
        } else {
            val player = gameInfo.findPlayer(userId)
            if (player != null) {
                player.connect()
            } else {
                gameInfo.room.add(MafiaPlayer(userId, nickname, generateColor(gameInfo)))
            }
        }

        mafiaGameRepository.saveGameInfo(gameInfo)
        mafiaPhaseMessenger.unicastPhase(userId, gameInfo)
        mafiaGameMessenger.broadcastPlayerList(gameInfo)
    }

    override fun connectSession(user: User, roomId: RoomId, locale: String) {
        var gameInfo = mafiaGameRepository.getGameInfo(roomId)

        if (gameInfo == null) {
            val player = MafiaPlayer(user.id, user.name, generateColor(null))

            gameInfo = createGameInfo(roomId, locale, player, true)
        } else {
            val room = gameInfo.room

            room.add(MafiaPlayer(user.id, user.name, generateColor(gameInfo)))
        }

        mafiaGameRepository.saveGameInfo(gameInfo)
    }

    override fun disconnectSession(userId: UserId) {
        val gameInfo = mafiaGameRepository.getGameInfo(userId) ?: return

        if (gameInfo.phase == MafiaPhase.Wait) {
            exitSession(userId)
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

    override fun exitSession(userId: UserId) {
        val gameInfo = mafiaGameRepository.getGameInfo(userId) ?: return

        if (gameInfo.phase != MafiaPhase.Wait) {
            disconnectSession(userId)
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

    private fun createGameInfo(roomId: RoomId, locale: String, player: MafiaPlayer, isRandomMatching: Boolean = false): MafiaGameInfo {
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
