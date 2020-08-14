package com.bcptest.exchange.util

sealed class ResultType<out F, out R> {
    data class Failure<out F>(val a: F) : ResultType<F, Nothing>()
    data class Success<out R>(val b: R) : ResultType<Nothing, R>()

    val isSuccess get() = this is Success<R>
    val isFailure get() = this is Failure<F>

    fun <L> failure(a: L) =
        Failure(a)
    fun <R> succes(b: R) =
        Success(b)

    fun either(fnL: (F) -> Any, fnR: (R) -> Any): Any = when (this) {
        is Failure -> fnL(a)
        is Success -> fnR(b)
    }
}