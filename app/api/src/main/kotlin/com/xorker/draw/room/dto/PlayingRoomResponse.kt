package com.xorker.draw.room.dto

import com.xorker.draw.room.RoomId
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "접속중인 게임 정보 입니다.")
data class PlayingRoomResponse(

    @Schema(description = "게임 방 코드")
    val roomId: RoomId? = null,
)
