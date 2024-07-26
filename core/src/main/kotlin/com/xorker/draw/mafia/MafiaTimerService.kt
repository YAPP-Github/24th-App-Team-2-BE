package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidMafiaGamePlayingPhaseStatusException
import com.xorker.draw.mafia.event.MafiaReadyExpiredEvent
import com.xorker.draw.mafia.event.MafiaRoleExpiredEvent
import com.xorker.draw.mafia.event.MafiaRoundExpiredEvent
import com.xorker.draw.mafia.event.MafiaTurnExpiredEvent
import com.xorker.draw.timer.TimerRepository
import com.xorker.draw.user.UserId
import java.text.SimpleDateFormat
import java.util.concurrent.ConcurrentHashMap
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
internal class MafiaTimerService(
    private val timerRepository: TimerRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
) {
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss") // TODO delete

    @EventListener
    fun subscribeMafiaReadyExpiredEvent(event: MafiaReadyExpiredEvent) {
        println("ready expired event")
        println(formatter.format(System.currentTimeMillis()))

        val gameInfo = event.gameInfo
        val gameOption = gameInfo.gameOption

        // TODO broadcast ready expired event
        timerRepository.startTimer(gameOption.roleShowingTime, MafiaRoleExpiredEvent(gameInfo))
    }

    @EventListener
    fun subscribeMafiaRoleExpiredEvent(event: MafiaRoleExpiredEvent) {
        println("role expired event")
        println(formatter.format(System.currentTimeMillis()))
        val gameInfo = event.gameInfo
        val gameOption = gameInfo.gameOption

        // TODO broadcast role expired event
        timerRepository.startTimer(gameOption.roundShowingTime, MafiaRoundExpiredEvent(gameInfo))
    }

    @EventListener
    fun subscribeMafiaRoundExpiredEvent(event: MafiaRoundExpiredEvent) {
        println("round expired event")
        println(formatter.format(System.currentTimeMillis()))
        val gameInfo = event.gameInfo
        val gameOption = gameInfo.gameOption

        // TODO broadcast round expired event
        timerRepository.startTimer(gameOption.turnTime, MafiaTurnExpiredEvent(gameInfo))
    }

    @EventListener
    fun subscribeMafiaTurnExpiredEvent(event: MafiaTurnExpiredEvent) {
        println("turn expired event")
        println(formatter.format(System.currentTimeMillis()))
        val gameInfo = event.gameInfo
        val gameOption = gameInfo.gameOption

        val room = gameInfo.room
        val phase = gameInfo.phase as? MafiaPhase.Playing ?: throw InvalidMafiaGamePlayingPhaseStatusException

        val currentRound = phase.round

        val currentTurn = phase.turn
        val nextTurn = (currentTurn + 1) % room.size()

        println("현재 라운드 = $currentRound, 현재 턴 = $currentTurn")

        if (currentTurn == room.size() - 1) {
            if (currentRound == gameOption.numTurn) {
                // TODO broadcast turn expired event
                val players = ConcurrentHashMap<UserId, MutableSet<UserId>>()
                phase.turnList.forEach {
                    players[it.userId] = mutableSetOf()
                }

                gameInfo.phase = MafiaPhase.Vote(players)
                // TODO 투표 expired timer start
                println("투표 화면으로 이동")
            } else {
                phase.turn = nextTurn
                phase.round = currentRound + 1
                // TODO broadcast turn expired event
                timerRepository.startTimer(gameOption.roundShowingTime, MafiaRoundExpiredEvent(gameInfo))
            }
        } else {
            phase.turn = nextTurn
            // TODO broadcast turn expired event
            timerRepository.startTimer(gameOption.turnTime, MafiaTurnExpiredEvent(gameInfo))
        }
    }
}
