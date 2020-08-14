package com.bcptest.exchange.ui.component

sealed class UIExchangeState {
    class ExchangeSuccessFrom(val result: Float): UIExchangeState()
    class ExchangeSuccessTo(val result: Float): UIExchangeState()
    object  ExchangeError : UIExchangeState()
}