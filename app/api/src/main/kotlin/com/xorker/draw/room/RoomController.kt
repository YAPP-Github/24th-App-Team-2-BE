package com.xorker.draw.room

import com.xorker.draw.mafia.MafiaGameUseCase
import com.xorker.draw.room.dto.PlayingRoomResponse
import com.xorker.draw.support.auth.PrincipalUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "게임 관련 APIs")
@RestController
class RoomController(
    private val mafiaGameUseCase: MafiaGameUseCase,
) {

    @Operation(summary = "현재 참여 중인 방 정보")
    @GetMapping("/api/v1/playing-room")
    fun getPlayingRoom(
        @Parameter(hidden = true) user: PrincipalUser,
    ): PlayingRoomResponse {
        val gameInfo = mafiaGameUseCase.getGameInfo(user.userId) ?: return PlayingRoomResponse()

        return PlayingRoomResponse(gameInfo.room.id)
    }
}
