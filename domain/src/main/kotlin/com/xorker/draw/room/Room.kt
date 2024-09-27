package com.xorker.draw.room

import com.xorker.draw.exception.AlreadyJoinRoomException
import com.xorker.draw.exception.MaxRoomException
import com.xorker.draw.user.UserId

@JvmInline
value class RoomId private constructor(val value: String) {
    companion object {
        operator fun invoke(value: String): RoomId {
            return RoomId(value)
        }

        operator fun invoke(value: String?): RoomId? {
            if (value == null) return null
            return RoomId(value)
        }
    }
}

interface Room<P : Player> {
    val id: RoomId
    val locale: String
    var owner: P
    val maxMemberNum: Int
    val players: List<P>
    val isRandomMatching: Boolean

    fun size(): Int = players.size
    fun isEmpty(): Boolean = size() == 0
    fun isFull(): Boolean = size() == maxMemberNum
    fun findPlayer(userId: UserId): P? = players.firstOrNull { it.userId == userId }
    fun add(player: P)
    fun remove(player: P)
    fun addAll(players: List<P>)
    fun clear()
    fun copy(): Room<P>
}

fun <P : Player> Room(
    id: RoomId,
    locale: String,
    owner: P,
    maxMemberNum: Int,
    players: MutableList<P> = mutableListOf(owner),
    isRandomMatching: Boolean = false,
): Room<P> = RoomImpl(id, locale, owner, maxMemberNum, players, isRandomMatching)

class RoomImpl<P : Player>(
    override val id: RoomId,
    override val locale: String,
    override var owner: P,
    override val maxMemberNum: Int,
    private val _players: MutableList<P> = mutableListOf(owner),
    override val isRandomMatching: Boolean,
) : Room<P> {
    override val players: List<P> = _players

    override fun add(player: P) {
        if (isFull()) {
            throw MaxRoomException
        }
        if (findPlayer(player.userId) != null) {
            throw AlreadyJoinRoomException
        }
        _players.add(player)
    }

    override fun addAll(players: List<P>) {
        players.forEach { add(it) }
    }

    override fun remove(player: P) {
        _players.remove(player)
    }

    override fun clear() {
        _players.clear()
    }

    override fun copy(): Room<P> = RoomImpl(id, locale, owner, maxMemberNum, _players, isRandomMatching)
}
