package com.bcptest.exchange.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class UseCase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): ResultType<Failure, Type>

    suspend operator fun invoke(
        params: Params,
        onResult: (ResultType<Failure, Type>) -> Unit = {}
    ) {
        val result = withContext(Dispatchers.IO) {
            run(params)
        }
    }

}
