package com.xorker.draw.exception

data class ExceptionResponse(
    val code: String,
    val dialog: DialogResponse?,
    val toast: ToastResponse?,
)

sealed class ExceptionDesign

data class ToastResponse(
    val text: String,
) : ExceptionDesign()

data class DialogResponse(
    val title: String? = null,
    val description: String,
    val buttons: List<ExceptionButtonResponse>,
) : ExceptionDesign()

data class ExceptionButtonResponse(
    val text: String,
    val action: ExceptionButtonAction,
)

sealed class ExceptionButtonAction(
    val type: String,
    description: String,
) {
    data object ForceUpdate : ExceptionButtonAction("FORCE_UPDATE", "강제 업데이트")
    data object CloseDialog : ExceptionButtonAction("DIALOG_CLOSE", "에러 팝업 닫기") // TODO: 클라랑 명칭 맞추기
    class OpenWebView(val url: String) : ExceptionButtonAction("OPEN_WEB_VIEW", "웹뷰 열기")

    override fun toString(): String {
        return type
    }
}
