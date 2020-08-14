package com.bcptest.exchange.ui.presenter.exchange

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bcptest.exchange.domain.usecase.ProcessUseCase
import com.bcptest.exchange.util.Failure
import com.bcptest.exchange.util.ManagerScreenState
import kotlinx.coroutines.launch

class ExchangeViewModel(private val processUseCase: ProcessUseCase) : ViewModel() {

    private var _state: MutableLiveData<ManagerScreenState<ExchangeState>> = MutableLiveData()

    val state: LiveData<ManagerScreenState<ExchangeState>>
        get() = _state

    //Envía a adicionar un counter
    fun proccess(  amountExchange: Float,
                   amountReference: Float,
                   currencyFrom: String,
                   currencyTo: String) {
        _state.value = ManagerScreenState.Loading
        viewModelScope.launch {
            processUseCase.run(ProcessUseCase.Params(amountExchange,amountReference,currencyFrom,currencyTo))
                .either(::error, ::response)
        }
    }

    private fun response(unit: Unit) {
        print(unit)
        _state.value = ManagerScreenState.Render(ExchangeState.Processs)
    }

    private fun error(failure: Failure) {
        _state.value =
            ManagerScreenState.Render(
                ExchangeState.Error)
    }

}