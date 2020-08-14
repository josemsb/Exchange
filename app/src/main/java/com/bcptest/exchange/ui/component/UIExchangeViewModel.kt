package com.bcptest.exchange.ui.component

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bcptest.exchange.ui.component.entity.Country
import com.bcptest.exchange.ui.component.util.UIExchangeState
import com.bcptest.exchange.util.ManagerScreenState
import kotlinx.coroutines.launch

class UIExchangeViewModel() : ViewModel() {

    private var _state: MutableLiveData<ManagerScreenState<UIExchangeState>> = MutableLiveData()
    val state: LiveData<ManagerScreenState<UIExchangeState>>
        get() = _state

    fun getExchangeFromaTo(
        mount: Float,
        exchangeCurrencyFrom: String,
        exchangeCurrencyTo: String,
        countries: List<Country>
    ) {
        _state.value = ManagerScreenState.Loading
        viewModelScope.launch {
            try {

                var mountPivot = 0.0f
                var newMounConvertPivot = 0.0f
                var mountConvertFinal = 0.0f

                countries.forEach {
                    if (it.money == exchangeCurrencyFrom) {
                        mountPivot = it.valor
                        newMounConvertPivot = mount * (1 / mountPivot)
                    }

                }

                countries.forEach {
                    if (it.money == exchangeCurrencyTo) {
                        mountPivot = it.valor
                        mountConvertFinal = newMounConvertPivot * mountPivot
                    }

                }

                _state.value =
                    ManagerScreenState.Render(UIExchangeState.ExchangeSuccessFrom(mountConvertFinal))


            } catch (e: Exception) {
                _state.value =
                    ManagerScreenState.Render(UIExchangeState.ExchangeError)

            }
        }
    }

    fun getExchangeToaFrom(
        mount: Float,
        exchangeCurrencyFrom: String,
        exchangeCurrencyTo: String,
        countries: List<Country>
    ) {
        _state.value = ManagerScreenState.Loading
        viewModelScope.launch {
            try {

                var mountPivot = 0.0f
                var newMounConvertPivot = 0.0f
                var mountConvertFinal = 0.0f

                countries.forEach {
                    if (it.money == exchangeCurrencyTo) {
                        mountPivot = it.valor
                        newMounConvertPivot = mount * (1 / mountPivot)
                    }

                }

                countries.forEach {
                    if (it.money == exchangeCurrencyFrom) {
                        mountPivot = it.valor
                        mountConvertFinal = newMounConvertPivot * mountPivot
                    }

                }

                _state.value =
                    ManagerScreenState.Render(UIExchangeState.ExchangeSuccessTo(mountConvertFinal))


            } catch (e: Exception) {
                _state.value =
                    ManagerScreenState.Render(UIExchangeState.ExchangeError)

            }
        }
    }
}