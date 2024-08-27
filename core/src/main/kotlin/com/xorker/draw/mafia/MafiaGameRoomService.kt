package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.exception.NotFoundRoomException
import com.xorker.draw.room.Room
import com.xorker.draw.websocket.Session
import com.xorker.draw.websocket.SessionEventListener
import com.xorker.draw.websocket.SessionInitializeRequest
import org.springframework.stereotype.Service

@Service
internal class MafiaGameRoomService(
    private val mafiaGameRepository: MafiaGameRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
    private val mafiaPhaseMessenger: MafiaPhaseMessenger,
) : SessionEventListener {

    override fun connectSession(session: Session, request: SessionInitializeRequest) {
        var gameInfo = mafiaGameRepository.getGameInfo(session.roomId)
        val userId = session.user.id

        if (gameInfo == null) {
            if (request.roomId != null) throw NotFoundRoomException
            val player = MafiaPlayer(userId, request.nickname, generateColor(null))
            gameInfo = createGameInfo(session, request.locale, player)
        } else {
            val player = gameInfo.findPlayer(userId)
            if (player != null) {
                player.connect()
            } else {
                gameInfo.room.add(MafiaPlayer(userId, request.nickname, generateColor(gameInfo)))
            }
        }

        mafiaGameRepository.saveGameInfo(gameInfo)
        mafiaPhaseMessenger.unicastPhase(session.user.id, gameInfo)
        mafiaGameMessenger.broadcastPlayerList(gameInfo)
    }

    override fun connectSession(session: Session, locale: String) {
        var gameInfo = mafiaGameRepository.getGameInfo(session.roomId)
        val user = session.user

        if (gameInfo == null) {
            val player = MafiaPlayer(user.id, user.name, generateColor(null))

            gameInfo = createGameInfo(session, locale, player, true)
        } else {
            val room = gameInfo.room

            room.add(MafiaPlayer(user.id, user.name, generateColor(gameInfo)))
        }

        mafiaGameRepository.saveGameInfo(gameInfo)
    }

    override fun disconnectSession(session: Session) {
        val gameInfo = mafiaGameRepository.getGameInfo(session.roomId) ?: return

        if (gameInfo.phase == MafiaPhase.Wait) {
            exitSession(session)
            return
        }

        val player = gameInfo.findPlayer(session.user.id) ?: return

        player.disconnect()

        if (gameInfo.room.players.all { it.isConnect().not() }) {
            mafiaGameRepository.removeGameInfo(gameInfo)
        } else {
            mafiaGameRepository.saveGameInfo(gameInfo)
            mafiaGameMessenger.broadcastPlayerList(gameInfo)
        }
    }

    override fun exitSession(session: Session) {
        val gameInfo = mafiaGameRepository.getGameInfo(session.roomId) ?: return

        if (gameInfo.phase != MafiaPhase.Wait) {
            disconnectSession(session)
            return
        }

        val player = gameInfo.findPlayer(session.user.id) ?: return

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

    private fun createGameInfo(session: Session, locale: String, player: MafiaPlayer, isRandomMatching: Boolean = false): MafiaGameInfo {
        val room = createRoom(session, locale, player, isRandomMatching)
        return MafiaGameInfo(
            room = room,
            phase = MafiaPhase.Wait,
            gameOption = MafiaGameOption(),
        )
    }

    private fun createRoom(session: Session, locale: String, player: MafiaPlayer, isRandomMatching: Boolean): Room<MafiaPlayer> {
        val language = locale.lowercase()
        if (language !in languages) {
            throw InvalidRequestValueException
        }

        val room = Room(
            id = session.roomId,
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
