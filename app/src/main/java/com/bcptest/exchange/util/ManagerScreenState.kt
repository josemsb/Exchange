package com.bcptest.exchange.util

sealed class ManagerScreenState<out T> {
    object  Loading: ManagerScreenState<Nothing>()
    class Render<T>(val renderState: T): ManagerScreenState<T>()
}