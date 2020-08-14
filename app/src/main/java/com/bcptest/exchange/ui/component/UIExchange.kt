package com.bcptest.exchange.ui.component

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.bcptest.exchange.R
import com.bcptest.exchange.ui.component.entity.Country
import com.bcptest.exchange.ui.component.entity.ExchangeType
import com.bcptest.exchange.ui.component.repository.ActivityResultObservable
import com.bcptest.exchange.ui.component.repository.ActivityResultObserver
import com.bcptest.exchange.ui.component.shared.SharedStorage
import com.bcptest.exchange.ui.component.ui.SelectedCountry
import com.bcptest.exchange.ui.component.util.LifecycleOwnerNotFoundException
import com.bcptest.exchange.ui.component.util.UIExchangeState
import com.bcptest.exchange.util.ManagerScreenState
import kotlinx.android.synthetic.main.view_exchange_component.view.*


private const val GET_COUNTRY = 1001

class UIExchange constructor(context: Context?, attributes: AttributeSet?) :
    FrameLayout(context!!, attributes), ActivityResultObserver {

    var mountExchange: Float = 0.0f
    var resultReference: Float = 0.0f
    var exchangeCurrencyFrom: String = ""
    var exchangeCurrencyTo: String = ""
    val viewModel = UIExchangeViewModel()
    private var exchangeType: ExchangeType? = null

    init {
        inflate(context, R.layout.view_exchange_component, this)
        initListener()
    }

    //Load data (Json or Storage)
    fun loadExchange(exchangeType: ExchangeType) {
        this.exchangeType = exchangeType
        if (isNotSaveData())
            loadJson()
        else
            loadStorage()
    }

    private fun initListener() {
        btnSend.setOnLongClickListener {
            selectedCountry("from")
            true
        }

        btnGo.setOnLongClickListener {
            selectedCountry("to")
            true
        }

        txtAmountFrom.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, st: Int, ct: Int,
                af: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (txtAmountFrom.isFocused) {
                    if (s.isNotEmpty())
                        viewModel.getExchangeFromaTo(
                            txtAmountFrom.text.toString().toFloat(),
                            exchangeCurrencyFrom,
                            exchangeCurrencyTo,
                            exchangeType!!.countries
                        )
                    else
                        txtAmountTo.setText("")
                }
            }
        })

        txtAmountTo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, st: Int, ct: Int,
                af: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (txtAmountTo.isFocused) {
                    if (s.isNotEmpty())
                        viewModel.getExchangeToaFrom(
                            txtAmountTo.text.toString().toFloat(),
                            exchangeCurrencyFrom,
                            exchangeCurrencyTo,
                            exchangeType!!.countries
                        )
                    else
                        txtAmountFrom.setText("")
                }
            }
        })

        frlRefresh.setOnClickListener {

            val currencyFrom = exchangeCurrencyTo
            val currencyTo = exchangeCurrencyFrom

            exchangeCurrencyFrom = currencyFrom
            exchangeCurrencyTo = currencyTo

            exchangeType!!.countries.forEach {
                if (it.money == exchangeCurrencyFrom) {
                    btnSend.text = it.country
                    txtFrom.text = context.resources.getString(R.string.toSend).plus(it.money)
                }

                if (it.money == exchangeCurrencyTo) {
                    txtTo.text = context.resources.getString(R.string.toGo).plus(it.money)
                    btnGo.text = it.country
                }
            }

            if (txtAmountFrom.text.isNotEmpty())
                viewModel.getExchangeFromaTo(
                    txtAmountFrom.text.toString().toFloat(),
                    exchangeCurrencyFrom,
                    exchangeCurrencyTo,
                    exchangeType!!.countries
                )
        }
    }

    //Show activity from selected country
    private fun selectedCountry(country: String) {
        val intent = Intent(context as Activity, SelectedCountry::class.java)
        intent.putExtra("exchangeType", this.exchangeType)
        intent.putExtra("exchange", country)
        (context as Activity).startActivityForResult(intent, GET_COUNTRY)
    }

    //Init observer live data
    private fun observeLiveData(lifecycleOwner: LifecycleOwner) {
        viewModel.state.observe(lifecycleOwner, Observer {
            when (it) {
                is ManagerScreenState.Loading -> Unit //implement loading
                is ManagerScreenState.Render -> processResult(it.renderState)
            }
        })
    }

    //Process state from view model
    private fun processResult(renderState: UIExchangeState) {
        when (renderState) {
            is UIExchangeState.ExchangeError -> Unit  //Implement dialog error
            is UIExchangeState.ExchangeSuccessFrom -> {
                txtAmountTo.clearFocus()
                txtAmountTo.setText(renderState.result.toString())
                saveData()
            }
            is UIExchangeState.ExchangeSuccessTo -> {
                txtAmountFrom.clearFocus()
                txtAmountFrom.setText(renderState.result.toString())
                saveData()
            }
        }
    }

    //Load data from json
    private fun loadJson() {
        exchangeType!!.countries.forEach {
            if (it.selectedSend) {
                txtFrom.text = context.resources.getString(R.string.toSend).plus(it.money)
                btnSend.text = it.country
                exchangeCurrencyFrom = it.money
                txtSummary.text =
                    context.resources.getString(R.string.sale).plus(it.purchase.toString())
                        .plus(" | venta: ")
                        .plus(it.sale.toString())
            }

            if (it.selectedGo) {
                txtTo.text = context.resources.getString(R.string.toGo).plus(it.money)
                btnGo.text = it.country
                exchangeCurrencyTo = it.money
            }
        }
    }

    //Load data from storage (init UI)
    private fun loadStorage() {
        txtAmountFrom.clearFocus()
        txtAmountFrom.setText(SharedStorage(context).amountFrom.toString())
        txtAmountTo.clearFocus()
        txtAmountTo.setText(SharedStorage(context).amountTo.toString())
        exchangeCurrencyFrom = SharedStorage(context).currencyFrom
        exchangeCurrencyTo = SharedStorage(context).currencyTo

        exchangeType!!.countries.forEach {
            if (it.money == exchangeCurrencyFrom) {
                txtFrom.text = context.resources.getString(R.string.toSend).plus(it.money)
                btnSend.text = it.country
                txtSummary.text =
                    context.resources.getString(R.string.sale).plus(it.purchase.toString())
                        .plus(" | venta: ")
                        .plus(it.sale.toString())
            }

            if (it.money == exchangeCurrencyTo){
                txtTo.text = context.resources.getString(R.string.toGo).plus(it.money)
                btnGo.text = it.country
            }


        }

    }

    //Save data
    private fun saveData() {
        mountExchange = txtAmountFrom.text.toString().toFloat()
        resultReference = txtAmountTo.text.toString().toFloat()
        SharedStorage(context).amountFrom = mountExchange
        SharedStorage(context).amountTo = resultReference
        SharedStorage(context).currencyFrom = exchangeCurrencyFrom
        SharedStorage(context).currencyTo = exchangeCurrencyTo
    }

    private fun isNotSaveData(): Boolean {
        return SharedStorage(context).currencyFrom == "" && SharedStorage(context).currencyTo == ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GET_COUNTRY && resultCode == Activity.RESULT_OK) {

            val country: Country = data?.getSerializableExtra("country") as Country
            val type: String = data.getStringExtra("exchange") as String
            country.let {
                if (type == "from") {
                    txtFrom.text = context.resources.getString(R.string.toSend).plus(it.money)
                    btnSend.text = it.country
                    exchangeCurrencyFrom = it.money
                    txtSummary.text =
                        context.resources.getString(R.string.sale).plus(it.purchase.toString())
                            .plus(" | venta: ")
                            .plus(it.sale.toString())
                    if (txtAmountFrom.text.isNotEmpty())
                        viewModel.getExchangeFromaTo(
                            txtAmountFrom.text.toString().toFloat(),
                            exchangeCurrencyFrom,
                            exchangeCurrencyTo,
                            exchangeType!!.countries
                        )
                } else {
                    btnGo.text = it.country
                    exchangeCurrencyTo = it.money
                    txtTo.text = context.resources.getString(R.string.toGo).plus(it.money)
                    if (txtAmountFrom.text.isNotEmpty())
                        viewModel.getExchangeFromaTo(
                            txtAmountFrom.text.toString().toFloat(),
                            exchangeCurrencyFrom,
                            exchangeCurrencyTo,
                            exchangeType!!.countries
                        )

                }
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        (context as ActivityResultObservable).addObserver(this)

        val lifecycleOwner = context as? LifecycleOwner ?: throw LifecycleOwnerNotFoundException()
        onLifecycleOwnerAttached(lifecycleOwner)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        (context as ActivityResultObservable).removeObserver(this)
    }

    private fun onLifecycleOwnerAttached(lifecycleOwner: LifecycleOwner) {
        observeLiveData(lifecycleOwner)
    }

}


