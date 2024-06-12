package com.xorker.xpr.exception

sealed class XprException(val code: String, message: String, cause: Throwable? = null) : RuntimeException(message, cause)

//region Client
sealed class ClientException(code: String, message: String, cause: Throwable? = null) : XprException(code, message, cause)

data object UnAuthenticationException : ClientException("auth401", "인증 실패") { private fun readResolve(): Any = UnAuthenticationException }
data object UnAuthorizedException : ClientException("auth403", "인가 실패") { private fun readResolve(): Any = UnAuthorizedException }

data object InvalidRequestValueException : ClientException("c001", "Request 값 잘못됨") { private fun readResolve(): Any = InvalidRequestValueException }
data object OAuthFailureException : ClientException("c002", "OAuth 인증 실패") { private fun readResolve(): Any = OAuthFailureException}
//endregion

//region Server
sealed class ServerException(code: String, message: String, cause: Throwable? = null) : XprException(code, message, cause)
//endregion

//region Critical
sealed class CriticalException(code: String, message: String, cause: Throwable? = null) : XprException(code, message, cause)

class UnknownException(cause: Throwable) : CriticalException("crt001", "정의하지 못한 예외", cause)
//endregion
