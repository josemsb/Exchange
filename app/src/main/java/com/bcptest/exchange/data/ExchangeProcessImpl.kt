package com.bcptest.exchange.data

import com.bcptest.exchange.domain.repository.ExchangeProcessRepository
import com.bcptest.exchange.util.Failure
import com.bcptest.exchange.util.ResultType

class ExchangeProcessImpl : ExchangeProcessRepository {
    override suspend fun process(
        amountExchange: Float,
        amountReference: Float,
        currencyFrom: String,
        currencyTo: String
    ): ResultType<Failure, Unit> {
        //Proceso de validacion de tipo de cambio
        return ResultType.Success(Unit)
    }

}