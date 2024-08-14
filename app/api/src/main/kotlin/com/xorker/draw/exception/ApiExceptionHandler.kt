package com.xorker.draw.exception

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

typealias ExceptionResponseEntity = ResponseEntity<ExceptionResponse>

@ControllerAdvice
class ApiExceptionHandler(
    private val responseFactory: ExceptionResponseFactory,
) {
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    protected fun handleException(ex: HttpRequestMethodNotSupportedException): ExceptionResponseEntity {
        return responseFactory.create(HttpStatus.METHOD_NOT_ALLOWED, InvalidRequestValueException)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    protected fun handleException(ex: MethodArgumentTypeMismatchException): ExceptionResponseEntity {
        return responseFactory.create(InvalidRequestValueException)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleException(ex: MethodArgumentNotValidException): ExceptionResponseEntity {
        return responseFactory.create(InvalidRequestValueException)
    }

    @ExceptionHandler(BindException::class)
    protected fun handleException(ex: BindException): ExceptionResponseEntity {
        return responseFactory.create(InvalidRequestValueException)
    }

    @ExceptionHandler(XorkerException::class)
    protected fun handleException(ex: XorkerException): ExceptionResponseEntity {
        return responseFactory.create(ex)
    }

    @ExceptionHandler(CriticalException::class)
    protected fun handleException(ex: CriticalException): ExceptionResponseEntity {
        // TODO: Alert Development , Discord Webhook or Sentry
        return responseFactory.create(ex)
    }

    @Order(value = Ordered.LOWEST_PRECEDENCE)
    @ExceptionHandler(Exception::class)
    protected fun handleException(ex: Exception): ResponseEntity<ExceptionResponse> {
        ex.printStackTrace()
        return responseFactory.create(UnknownException(ex))
    }
}
