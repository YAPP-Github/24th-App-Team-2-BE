package com.xorker.xpr.exception

data class ExceptionResponse(
    val code: String,
    val title: String? = null,
    val description: String? = null,
    val buttons: List<ExceptionButtonResponse>? = null,
)

data class ExceptionButtonResponse(
    val text: String,
)
