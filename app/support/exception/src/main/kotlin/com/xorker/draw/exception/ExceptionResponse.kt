package com.xorker.draw.exception

data class ExceptionResponse(
    val code: String,
    val title: String? = null,
    val description: String? = null,
    val buttons: List<ExceptionButtonResponse>? = null,
)

data class ExceptionButtonResponse(
    val text: String,
    val action: ExceptionButtonAction,
)

sealed class ExceptionButtonAction(private val type: String, description: String) {
    data object CloseDialog : ExceptionButtonAction("DIALOG_CLOSE", "에러 팝업 닫기") // TODO: 클라랑 명칭 맞추기
    class OpenWebView(val url: String) : ExceptionButtonAction("OPEN_WEB_VIEW", "웹뷰 열기")

    override fun toString(): String {
        return type
    }
}
