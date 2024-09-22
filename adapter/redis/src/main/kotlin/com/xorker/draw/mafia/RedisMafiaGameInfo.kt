package com.xorker.draw.mafia

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.xorker.draw.mafia.turn.TurnInfo
import com.xorker.draw.room.Room
import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId
import java.time.Duration
import java.util.*

data class RedisMafiaGameInfo @JsonCreator constructor(
    @JsonProperty("room") val room: RedisRoom,
    @JsonProperty("phase") val phase: RedisMafiaPhase,
    @JsonProperty("gameOption") val gameOption: RedisGameOption,
) {
    fun toMafiaGameInfo(): MafiaGameInfo = MafiaGameInfo(
        room = room.toRoom(),
        phase = phase.toMafiaPhase(),
        gameOption = gameOption.toGameOption(),
    )
}

data class RedisRoom @JsonCreator constructor(
    @JsonProperty("id") val id: String,
    @JsonProperty("locale") val locale: String,
    @JsonProperty("owner") val owner: RedisMafiaPlayer,
    @JsonProperty("maxMemberNum") val maxMemberNum: Int,
    @JsonProperty("players") val players: List<RedisMafiaPlayer>,
    @JsonProperty("randomMatching") val isRandomMatching: Boolean,
)

data class RedisMafiaPlayer @JsonCreator constructor(
    @JsonProperty("id") val id: Long,
    @JsonProperty("nickname") val nickname: String,
    @JsonProperty("color") val color: String,
    @JsonProperty("connect") val isConnect: Boolean,
)

data class RedisGameOption @JsonCreator constructor(
    @JsonProperty("minimum") val minimum: Int,
    @JsonProperty("maximum") val maximum: Int,
    @JsonProperty("readyTime") val readyTime: Long,
    @JsonProperty("introAnimationTime") val introAnimationTime: Long,
    @JsonProperty("roundAnimationTime") val roundAnimationTime: Long,
    @JsonProperty("round") val round: Int,
    @JsonProperty("turnTime") val turnTime: Long,
    @JsonProperty("turnCount") val turnCount: Int,
    @JsonProperty("voteTime") val voteTime: Long,
    @JsonProperty("answerTime") val answerTime: Long,
    @JsonProperty("endTime") val endTime: Long,
)

data class RedisMafiaPhase @JsonCreator constructor(
    @JsonProperty("status") val status: String,
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

data class RedisMafiaKeyword @JsonCreator constructor(
    @JsonProperty("category") val category: String,
    @JsonProperty("answer") val answer: String,
)

fun MafiaGameInfo.toRedisMafiaGameInfo(): RedisMafiaGameInfo = RedisMafiaGameInfo(
    room = RedisRoom(
        id = room.id.value,
        locale = room.locale,
        owner = RedisMafiaPlayer(
            id = room.owner.userId.value,
            nickname = room.owner.nickname,
            color = room.owner.color,
            isConnect = room.owner.isConnect(),
        ),
        maxMemberNum = room.maxMemberNum,
        players = room.players.map { player ->
            RedisMafiaPlayer(
                id = player.userId.value,
                nickname = player.nickname,
                color = player.color,
                isConnect = player.isConnect(),
            )
        },
        isRandomMatching = room.isRandomMatching,
    ),
    phase = phase.toRedisMafiaPhase(),
    gameOption = RedisGameOption(
        minimum = gameOption.minimum,
        maximum = gameOption.maximum,
        readyTime = gameOption.readyTime.toMillis(),
        introAnimationTime = gameOption.introAnimationTime.toMillis(),
        roundAnimationTime = gameOption.roundAnimationTime.toMillis(),
        round = gameOption.round,
        turnTime = gameOption.turnTime.toMillis(),
        turnCount = gameOption.turnCount,
        voteTime = gameOption.voteTime.toMillis(),
        answerTime = gameOption.answerTime.toMillis(),
        endTime = gameOption.endTime.toMillis(),
    ),
)

fun MafiaPhase.toRedisMafiaPhase(): RedisMafiaPhase {
    return when (this) {
        is MafiaPhase.Wait -> RedisMafiaPhase(
            status = "wait",
        )

        is MafiaPhase.Ready -> RedisMafiaPhase(
            status = "ready",
            jobKey = jobKey.toString(),
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
            status = "playing",
            jobKey = jobKey.toString(),
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
            status = "vote",
            jobKey = jobKey.toString(),
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
            status = "inferAnswer",
            jobKey = jobKey.toString(),
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
            status = "end",
            jobKey = jobKey.toString(),
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

fun RedisMafiaPhase.toMafiaPhase(): MafiaPhase {
    return when (status) {
        "wait" -> MafiaPhase.Wait
        "ready" -> MafiaPhase.Ready(
            jobKey = RoomId(jobKey!!),
            turnList = turnList!!.map { player ->
                player.toPlayer()
            },
            mafiaPlayer = mafiaPlayer!!.toPlayer(),
            keyword = keyword!!.toMafiaKeyword(),
        )

        "playing" -> MafiaPhase.Playing(
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

        "vote" -> MafiaPhase.Vote(
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

        "inferAnswer" -> MafiaPhase.InferAnswer(
            jobKey = RoomId(jobKey!!),
            turnList = turnList!!.map { player ->
                player.toPlayer()
            },
            mafiaPlayer = mafiaPlayer!!.toPlayer(),
            keyword = keyword!!.toMafiaKeyword(),
            drawData = drawData!!.map { pair ->
                Pair(UserId(pair.first), pair.second)
            }.toMutableList(),
            answer = answer!!,
        )

        "end" -> MafiaPhase.End(
            jobKey = RoomId(jobKey!!),
            turnList = turnList!!.map { player ->
                player.toPlayer()
            },
            mafiaPlayer = mafiaPlayer!!.toPlayer(),
            keyword = keyword!!.toMafiaKeyword(),
            drawData = drawData!!.map { pair ->
                Pair(UserId(pair.first), pair.second)
            }.toMutableList(),
            answer = answer!!,
            showAnswer = showAnswer!!,
            isMafiaWin = isMafiaWin!!,
        )

        else -> MafiaPhase.Wait
    }
}

fun RedisRoom.toRoom(): Room<MafiaPlayer> = Room(
    id = RoomId(this.id),
    locale = this.locale,
    owner = this.owner.toPlayer(),
    maxMemberNum = this.maxMemberNum,
    players = this.players.map { player ->
        player.toPlayer()
    }.toMutableList(),
    isRandomMatching = isRandomMatching,
)

fun RedisMafiaPlayer.toPlayer(): MafiaPlayer {
    val mafiaPlayer = MafiaPlayer(
        userId = UserId(this.id),
        nickname = this.nickname,
        color = this.color,
    )

    if (this.isConnect.not()) {
        mafiaPlayer.disconnect()
    }

    return mafiaPlayer
}

fun RedisGameOption.toGameOption(): MafiaGameOption = MafiaGameOption(
    minimum = this.minimum,
    maximum = this.maximum,
    readyTime = Duration.ofMillis(this.readyTime),
    introAnimationTime = Duration.ofMillis(this.introAnimationTime),
    roundAnimationTime = Duration.ofMillis(this.roundAnimationTime),
    round = this.round,
    turnTime = Duration.ofMillis(this.turnTime),
    turnCount = this.turnCount,
    voteTime = Duration.ofMillis(this.voteTime),
    answerTime = Duration.ofMillis(this.answerTime),
    endTime = Duration.ofMillis(this.endTime),
)

fun RedisMafiaKeyword.toMafiaKeyword(): MafiaKeyword = MafiaKeyword(
    answer = this.answer,
    category = this.category,
)

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
