package com.bcptest.exchange.domain.usecase

import com.bcptest.exchange.domain.repository.ExchangeProcessRepository
import com.bcptest.exchange.util.Failure
import com.bcptest.exchange.util.ResultType
import com.bcptest.exchange.util.UseCase

class ProcessUseCase(private val exchangeProcessRepository: ExchangeProcessRepository) :
    UseCase<Unit, ProcessUseCase.Params>() {

    data class Params(
        val amountExchange: Float,
        val amountReference: Float,
        val currencyFrom: String,
        val currencyTo: String
    )

    override suspend fun run(params: Params): ResultType<Failure, Unit> {
        return exchangeProcessRepository.process(
            params.amountExchange,
            params.amountReference,
            params.currencyFrom,
            params.currencyTo
        )
    }

}