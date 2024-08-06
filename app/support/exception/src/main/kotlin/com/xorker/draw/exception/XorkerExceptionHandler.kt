package com.xorker.draw.exception

import org.springframework.stereotype.Component

@Component
class XorkerExceptionHandler(
    private val exceptionDesignFactory: ExceptionDesignFactory,
) {

    fun convert(ex: XorkerException): ExceptionResponse {
        return when (val design = exceptionDesignFactory.generate(ex)) {
            is DialogResponse -> ExceptionResponse(ex.code, dialog = design, toast = null)
            is ToastResponse -> ExceptionResponse(ex.code, toast = design, dialog = null)
        }
    }
}
