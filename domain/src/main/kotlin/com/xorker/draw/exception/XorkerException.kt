package com.xorker.draw.exception

sealed class XorkerException(val code: String, message: String, cause: Throwable? = null) : RuntimeException(message, cause)

//region Client
sealed class ClientException(code: String, message: String, cause: Throwable? = null) : XorkerException(code, message, cause)

data object UnAuthenticationException : ClientException("auth401", "인증 실패") { private fun readResolve(): Any = UnAuthenticationException }
data object UnAuthorizedException : ClientException("auth403", "인가 실패") { private fun readResolve(): Any = UnAuthorizedException }

data object InvalidRequestValueException : ClientException("c001", "Request 값 잘못됨") { private fun readResolve(): Any = InvalidRequestValueException }
data object OAuthFailureException : ClientException("c002", "OAuth 인증 실패") { private fun readResolve(): Any = OAuthFailureException }
data object NotFoundRoomException : ClientException("c003", "존재하지 않는 Room Id") { private fun readResolve(): Any = NotFoundRoomException }
data object MaxRoomException : ClientException("c004", "인원 수가 가득 찬 Room Id") { private fun readResolve(): Any = MaxRoomException }
data object AlreadyJoinRoomException : ClientException("c005", "이미 참여한 방") { private fun readResolve(): Any = AlreadyJoinRoomException }
//endregion

//region Server
sealed class ServerException(code: String, message: String, cause: Throwable? = null) : XorkerException(code, message, cause)

data object NotFoundUserException : ClientException("s001", "유저가 존재하지 않음") { private fun readResolve(): Any = NotFoundUserException }
//endregion

//region Critical
sealed class CriticalException(code: String, message: String, cause: Throwable? = null) : XorkerException(code, message, cause)

class UnknownException(cause: Throwable) : CriticalException("crt001", "정의하지 못한 예외", cause)
data object InvalidUserStatusException : CriticalException("crt002", "유효하지 않는 상태를 가진 유저를 조회함") { private fun readResolve(): Any = InvalidUserStatusException }
//endregion
