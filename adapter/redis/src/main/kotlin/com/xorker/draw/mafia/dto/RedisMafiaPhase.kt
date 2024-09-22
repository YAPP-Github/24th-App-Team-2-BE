package com.xorker.draw.mafia.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.mafia.turn.TurnInfo
import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId
import java.util.*

data class RedisMafiaPhase @JsonCreator constructor(
    @JsonProperty("status") val status: RedisMafiaPhaseStatus,
    @JsonProperty("jobKey") val jobKey: String? = null,
    @JsonProperty("turnList") val turnList: List<RedisMafiaPlayer>? = null,
    @JsonProperty("mafiaPlayer") val mafiaPlayer: RedisMafiaPlayer? = null,
    @JsonProperty("keyword") val keyword: RedisMafiaKeyword? = null,
    @JsonProperty("drawData") val drawData: List<Pair<Long, Map<String, Any>>>? = null,
    @JsonProperty("players") val players: Map<Long, List<Long>>? = null,
    @JsonProperty("round") val round: Int? = null,
    @JsonProperty("turn") val turn: Int? = null,
    @JsonProperty("answer") val answer: String? = null,
    @JsonProperty("showAnswer") val showAnswer: Boolean? = null,
    @JsonProperty("mafiaWin") val isMafiaWin: Boolean? = null,
)

enum class RedisMafiaPhaseStatus {
    WAIT,
    READY,
    PLAYING,
    VOTE,
    INFER_ANSWER,
    END,
    ;
}

fun serializePlayers(players: Map<UserId, Vector<UserId>>): Map<Long, List<Long>> {
    val serializedPlayers = mutableMapOf<Long, MutableList<Long>>()
    players.forEach { player ->
        serializedPlayers[player.key.value] = mutableListOf()
        players[player.key]?.forEach { userId ->
            serializedPlayers[player.key.value]?.add(userId.value)
        }
    }
    return serializedPlayers
}

fun deserializePlayers(players: Map<Long, List<Long>>): Map<UserId, Vector<UserId>> {
    val deserializedPlayers = mutableMapOf<UserId, Vector<UserId>>()
    players.forEach { player ->
        deserializedPlayers[UserId(player.key)] = Vector()
        players[player.key]?.forEach { userId ->
            deserializedPlayers[UserId(player.key)]?.add(UserId(userId))
        }
    }
    return deserializedPlayers
}

fun MafiaPhase.toRedisMafiaPhase(): RedisMafiaPhase {
    return when (this) {
        is MafiaPhase.Wait -> RedisMafiaPhase(
            status = RedisMafiaPhaseStatus.WAIT,
        )

        is MafiaPhase.Ready -> RedisMafiaPhase(
            status = RedisMafiaPhaseStatus.READY,
            jobKey = jobKey.value,
            turnList = turnList.map { player ->
                RedisMafiaPlayer(
                    id = player.userId.value,
                    nickname = player.nickname,
                    color = player.color,
                    isConnect = player.isConnect(),
                )
            },
            mafiaPlayer = RedisMafiaPlayer(
                id = mafiaPlayer.userId.value,
                nickname = mafiaPlayer.nickname,
                color = mafiaPlayer.color,
                isConnect = mafiaPlayer.isConnect(),
            ),
            keyword = RedisMafiaKeyword(
                answer = keyword.answer,
                category = keyword.category,
            ),
        )

        is MafiaPhase.Playing -> RedisMafiaPhase(
            status = RedisMafiaPhaseStatus.PLAYING,
            jobKey = jobKey.value,
            turnList = turnList.map { player ->
                RedisMafiaPlayer(
                    id = player.userId.value,
                    nickname = player.nickname,
                    color = player.color,
                    isConnect = player.isConnect(),
                )
            },
            mafiaPlayer = RedisMafiaPlayer(
                id = mafiaPlayer.userId.value,
                nickname = mafiaPlayer.nickname,
                color = mafiaPlayer.color,
                isConnect = mafiaPlayer.isConnect(),
            ),
            keyword = RedisMafiaKeyword(
                answer = keyword.answer,
                category = keyword.category,
            ),
            drawData = drawData.map { pair ->
                Pair(pair.first.value, pair.second)
            },
            round = turnInfo.round,
            turn = turnInfo.turn,
        )

        is MafiaPhase.Vote -> RedisMafiaPhase(
            status = RedisMafiaPhaseStatus.VOTE,
            jobKey = jobKey.value,
            turnList = turnList.map { player ->
                RedisMafiaPlayer(
                    id = player.userId.value,
                    nickname = player.nickname,
                    color = player.color,
                    isConnect = player.isConnect(),
                )
            },
            mafiaPlayer = RedisMafiaPlayer(
                id = mafiaPlayer.userId.value,
                nickname = mafiaPlayer.nickname,
                color = mafiaPlayer.color,
                isConnect = mafiaPlayer.isConnect(),
            ),
            keyword = RedisMafiaKeyword(
                answer = keyword.answer,
                category = keyword.category,
            ),
            drawData = drawData.map { pair ->
                Pair(pair.first.value, pair.second)
            },
            players = serializePlayers(this.players),
        )

        is MafiaPhase.InferAnswer -> RedisMafiaPhase(
            status = RedisMafiaPhaseStatus.INFER_ANSWER,
            jobKey = jobKey.value,
            turnList = turnList.map { player ->
                RedisMafiaPlayer(
                    id = player.userId.value,
                    nickname = player.nickname,
                    color = player.color,
                    isConnect = player.isConnect(),
                )
            },
            mafiaPlayer = RedisMafiaPlayer(
                id = mafiaPlayer.userId.value,
                nickname = mafiaPlayer.nickname,
                color = mafiaPlayer.color,
                isConnect = mafiaPlayer.isConnect(),
            ),
            keyword = RedisMafiaKeyword(
                answer = keyword.answer,
                category = keyword.category,
            ),
            drawData = drawData.map { pair ->
                Pair(pair.first.value, pair.second)
            },
            answer = answer,
        )

        is MafiaPhase.End -> RedisMafiaPhase(
            status = RedisMafiaPhaseStatus.END,
            jobKey = jobKey.value,
            turnList = turnList.map { player ->
                RedisMafiaPlayer(
                    id = player.userId.value,
                    nickname = player.nickname,
                    color = player.color,
                    isConnect = player.isConnect(),
                )
            },
            mafiaPlayer = RedisMafiaPlayer(
                id = mafiaPlayer.userId.value,
                nickname = mafiaPlayer.nickname,
                color = mafiaPlayer.color,
                isConnect = mafiaPlayer.isConnect(),
            ),
            keyword = RedisMafiaKeyword(
                answer = keyword.answer,
                category = keyword.category,
            ),
            drawData = drawData.map { pair ->
                Pair(pair.first.value, pair.second)
            },
            answer = answer,
            showAnswer = showAnswer,
            isMafiaWin = isMafiaWin,
        )
    }
}

fun RedisMafiaPhase.toMafiaPhase(): MafiaPhase = when (status) {
    RedisMafiaPhaseStatus.WAIT -> MafiaPhase.Wait
    RedisMafiaPhaseStatus.READY -> MafiaPhase.Ready(
        jobKey = RoomId(jobKey!!),
        turnList = turnList!!.map { player ->
            player.toPlayer()
        },
        mafiaPlayer = mafiaPlayer!!.toPlayer(),
        keyword = keyword!!.toMafiaKeyword(),
    )

    RedisMafiaPhaseStatus.PLAYING -> MafiaPhase.Playing(
        jobKey = RoomId(jobKey!!),
        turnList = turnList!!.map { player ->
            player.toPlayer()
        },
        mafiaPlayer = mafiaPlayer!!.toPlayer(),
        keyword = keyword!!.toMafiaKeyword(),
        turnInfo = TurnInfo(round!!, turn!!),
        drawData = drawData!!.map { pair ->
            Pair(UserId(pair.first), pair.second)
        }.toMutableList(),
    )

    RedisMafiaPhaseStatus.VOTE -> MafiaPhase.Vote(
        jobKey = RoomId(jobKey!!),
        turnList = turnList!!.map { player ->
            player.toPlayer()
        },
        mafiaPlayer = mafiaPlayer!!.toPlayer(),
        keyword = keyword!!.toMafiaKeyword(),
        drawData = drawData!!.map { pair ->
            Pair(UserId(pair.first), pair.second)
        }.toMutableList(),
        players = deserializePlayers(players!!),
    )

    RedisMafiaPhaseStatus.INFER_ANSWER -> MafiaPhase.InferAnswer(
        jobKey = RoomId(jobKey!!),
        turnList = turnList!!.map { player ->
            player.toPlayer()
        },
        mafiaPlayer = mafiaPlayer!!.toPlayer(),
        keyword = keyword!!.toMafiaKeyword(),
        drawData = drawData!!.map { pair ->
            Pair(UserId(pair.first), pair.second)
        }.toMutableList(),
        answer = answer,
    )

    RedisMafiaPhaseStatus.END -> MafiaPhase.End(
        jobKey = RoomId(jobKey!!),
        turnList = turnList!!.map { player ->
            player.toPlayer()
        },
        mafiaPlayer = mafiaPlayer!!.toPlayer(),
        keyword = keyword!!.toMafiaKeyword(),
        drawData = drawData!!.map { pair ->
            Pair(UserId(pair.first), pair.second)
        }.toMutableList(),
        answer = answer,
        showAnswer = showAnswer!!,
        isMafiaWin = isMafiaWin!!,
    )
}
