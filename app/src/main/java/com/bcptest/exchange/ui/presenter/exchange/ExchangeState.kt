package com.bcptest.exchange.ui.presenter.exchange

sealed class ExchangeState {
    object Processs: ExchangeState()
    object Error: ExchangeState()
}