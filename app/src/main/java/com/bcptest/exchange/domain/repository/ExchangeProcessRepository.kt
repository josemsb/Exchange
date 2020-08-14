package com.bcptest.exchange.domain.repository

import com.bcptest.exchange.util.Failure
import com.bcptest.exchange.util.ResultType

interface ExchangeProcessRepository {
    suspend fun process(
        amountExchange: Float,
        amountReference: Float,
        currencyFrom: String,
        currencyTo: String
    ): ResultType<Failure, Unit>
}