package com.xorker.draw.room

import com.xorker.draw.exception.AlreadyJoinRoomException
import com.xorker.draw.exception.MaxRoomException
import com.xorker.draw.user.UserId

@JvmInline
value class RoomId(val value: String)

interface Room<P : Player> {
    val id: RoomId
    var owner: P
    val maxMemberNum: Int
    val players: List<P>

    fun size(): Int = players.size
    fun isEmpty(): Boolean = size() == 0
    fun isFull(): Boolean = size() == maxMemberNum
    fun findPlayer(userId: UserId): P? = players.firstOrNull { it.userId == userId }
    fun add(player: P)
    fun remove(player: P)
    fun copy(): Room<P>
}

fun <P : Player> Room(
    id: RoomId,
    owner: P,
    maxMemberNum: Int,
    players: MutableList<P> = mutableListOf(owner),
): Room<P> = RoomImpl(id, owner, maxMemberNum, players)

class RoomImpl<P : Player>(
    override val id: RoomId,
    override var owner: P,
    override val maxMemberNum: Int,
    private val _players: MutableList<P> = mutableListOf(owner),
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

    override fun remove(player: P) {
        _players.remove(player)
    }

    override fun copy(): Room<P> = RoomImpl(id, owner, maxMemberNum, _players)
}
