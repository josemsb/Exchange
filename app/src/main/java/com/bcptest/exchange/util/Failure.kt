package com.bcptest.exchange.util

sealed class Failure {
    object DefaultError: Failure()
}