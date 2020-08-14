package com.bcptest.exchange.util

sealed class Failure {
    object NetworkConnection: Failure()
    object DefaultError: Failure()
}