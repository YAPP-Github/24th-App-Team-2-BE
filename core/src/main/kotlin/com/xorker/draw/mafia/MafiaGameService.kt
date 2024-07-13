package com.xorker.draw.mafia

import com.xorker.draw.room.Room
import com.xorker.draw.websocket.Session
import com.xorker.draw.websocket.SessionEventListener
import org.springframework.stereotype.Component

@Component
internal class MafiaGameService(
    private val mafiaGameRepository: MafiaGameRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
) : SessionEventListener {

    override fun connectSession(session: Session, nickname: String) {
        var gameInfo = mafiaGameRepository.getGameInfo(session.roomId)

        if (gameInfo != null) {
            val player = gameInfo.findPlayer(session.user.id)

            if (player != null) {
                player.connect()
                return
            }
        }

        val player = MafiaPlayer(session.user.id, nickname, generateColor(gameInfo))

        if (gameInfo == null) {
            gameInfo = createGameInfo(session, player)
        } else {
            gameInfo.room.add(player)
        }

        mafiaGameRepository.saveGameInfo(gameInfo)
        mafiaGameMessenger.broadcastPlayerList(gameInfo.room)
    }

    override fun disconnectSession(session: Session) {
        val gameInfo = mafiaGameRepository.getGameInfo(session.roomId) ?: return

        if (gameInfo.phase == MafiaPhase.Wait) {
            exitSession(session)
            return
        }

        val player = gameInfo.findPlayer(session.user.id) ?: return

        player.disconnect()
        mafiaGameRepository.saveGameInfo(gameInfo)
    }

    override fun exitSession(session: Session) {
        val gameInfo = mafiaGameRepository.getGameInfo(session.roomId) ?: return

        if (gameInfo.phase != MafiaPhase.Wait) {
            disconnectSession(session)
            return
        }

        val player = gameInfo.findPlayer(session.user.id) ?: return

        gameInfo.room.remove(player)
        mafiaGameRepository.saveGameInfo(gameInfo)
    }

    private fun generateColor(gameInfo: MafiaGameInfo?): String {
        return "ffffff"
    }

    private fun createGameInfo(session: Session, player: MafiaPlayer): MafiaGameInfo {
        val room = createRoom(session, player)
        return MafiaGameInfo(
            room,
            MafiaPhase.Wait,
            MafiaGameOption(),
        )
    }

    private fun createRoom(session: Session, player: MafiaPlayer): Room<MafiaPlayer> {
        val room = Room(session.roomId, player, 10)
        return room
    }
}
