package com.xorker.draw.exception

import java.util.Locale
import org.springframework.context.NoSuchMessageException

sealed class XorkerException(val code: String, message: String, cause: Throwable? = null) : RuntimeException(message, cause)

//region Client
sealed class ClientException(code: String, message: String, cause: Throwable? = null) : XorkerException(code, message, cause)

data object NeedForceUpdateException : ClientException("forceUpdate", "인증 실패") { private fun readResolve(): Any = NeedForceUpdateException }
data object UnAuthenticationException : ClientException("auth401", "인증 실패") { private fun readResolve(): Any = UnAuthenticationException }
data object UnAuthorizedException : ClientException("auth403", "인가 실패") { private fun readResolve(): Any = UnAuthorizedException }

data object InvalidRequestValueException : ClientException("c001", "Request 값 잘못됨") { private fun readResolve(): Any = InvalidRequestValueException }
data object OAuthFailureException : ClientException("c002", "OAuth 인증 실패") { private fun readResolve(): Any = OAuthFailureException }
data object NotFoundRoomException : ClientException("c003", "존재하지 않는 Room Id") { private fun readResolve(): Any = NotFoundRoomException }
data object MaxRoomException : ClientException("c004", "인원 수가 가득 찬 Room Id") { private fun readResolve(): Any = MaxRoomException }
data object AlreadyJoinRoomException : ClientException("c005", "이미 참여한 방") { private fun readResolve(): Any = AlreadyJoinRoomException }
data object InvalidRequestOnlyMyTurnException : ClientException("c006", "요청자의 차례가 아니라서 처리 불가능") { private fun readResolve(): Any = InvalidRequestOnlyMyTurnException }
data object InvalidRequestOtherPlayingException : ClientException("c007", "진행 중인 게임 방이 있습니다.") { private fun readResolve(): Any = InvalidRequestOtherPlayingException }
data object AlreadyPlayingRoomException : ClientException("c008", "진행 중인 게임 방에는 참여할 수 없습니다.") { private fun readResolve(): Any = AlreadyPlayingRoomException }

//endregion

//region Server
sealed class ServerException(code: String, message: String, cause: Throwable? = null) : XorkerException(code, message, cause)

data object NotFoundUserException : ServerException("s001", "유저가 존재하지 않음") { private fun readResolve(): Any = NotFoundUserException }
data object NotFoundWordException : ServerException("s002", "단어가 존재하지 않음") { private fun readResolve(): Any = NotFoundWordException }
//endregion

//region Critical
sealed class CriticalException(code: String, message: String, cause: Throwable? = null) : XorkerException(code, message, cause)

class UnknownException(cause: Throwable) : CriticalException("crt001", "정의하지 못한 예외", cause)
data object InvalidUserStatusException : CriticalException("crt002", "유효하지 않는 상태를 가진 유저를 조회함") { private fun readResolve(): Any = InvalidUserStatusException }
data object UnSupportedException : CriticalException("crt003", "정의하지 않는 행위") { private fun readResolve(): Any = UnSupportedException }
data object InvalidBroadcastException : CriticalException("crt004", "유효하지 않은 브로드캐스트 상태") { private fun readResolve(): Any = InvalidBroadcastException }
class InvalidMafiaPhaseException(message: String) : CriticalException("crt005", message)
data object InvalidWebSocketStatusException : CriticalException("crt006", "웹 소켓 세션 상태가 유효하지 않음") { private fun readResolve(): Any = InvalidWebSocketStatusException }
class NotDefinedMessageCodeException(messageCode: String, locale: Locale, cause: NoSuchMessageException) : CriticalException("crt007", "정의되지 않은 메시지 코드 $locale $messageCode", cause)
//endregion
