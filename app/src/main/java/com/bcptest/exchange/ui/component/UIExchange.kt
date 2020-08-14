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
import com.bcptest.exchange.ui.component.ui.SelectedCountry
import com.bcptest.exchange.util.ManagerScreenState
import kotlinx.android.synthetic.main.view_exchange_component.view.*


private const val GET_COUNTRY = 1001


class UIExchange constructor(context: Context?, attributes: AttributeSet?) :
    FrameLayout(context!!, attributes), ActivityResultObserver {

    var mountExchange: Float = 0.0f
    var resultReference: Float = 0.0f
    var exchangeCurrencyFrom: String = ""
    var exchangeCurrencyTo: String = ""
    private var exchangeType: ExchangeType? = null

    val viewModel = UIExchangeViewModel()

    init {
        inflate(context, R.layout.view_exchange_component, this)
        initListener()
    }

    private fun initListener() {
        btnSend.setOnClickListener { selectedCountry("from") }

        btnGo.setOnClickListener { selectedCountry("to") }

        txtSend.addTextChangedListener(object : TextWatcher {
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
                if (txtSend.isFocused) {
                    if (s.isNotEmpty())
                        viewModel.getExchangeFromaTo(
                            txtSend.text.toString().toFloat(),
                            exchangeCurrencyFrom,
                            exchangeCurrencyTo,
                            exchangeType!!.countries
                        )
                    else
                        txtGo.setText("")
                }
            }
        })

        txtGo.addTextChangedListener(object : TextWatcher {
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
                if (txtGo.isFocused) {
                    if (s.isNotEmpty())
                        viewModel.getExchangeToaFrom(
                            txtGo.text.toString().toFloat(),
                            exchangeCurrencyFrom,
                            exchangeCurrencyTo,
                            exchangeType!!.countries
                        )
                    else
                        txtSend.setText("")
                }
            }
        })
    }

    private fun selectedCountry(country: String) {
        val intent = Intent(context as Activity, SelectedCountry::class.java)
        intent.putExtra("exchangeType", this.exchangeType)
        intent.putExtra("exchange", country)
        (context as Activity).startActivityForResult(intent, GET_COUNTRY)
    }

    fun loadExchange(exchangeType: ExchangeType) {
        this.exchangeType = exchangeType
        exchangeType.countries.forEach {
            if (it.selectedSend) {
                btnSend.text = it.country
                exchangeCurrencyFrom = it.money
            }

            if (it.selectedGo) {
                btnGo.text = it.country
                exchangeCurrencyTo = it.money
            }
        }
    }

    private fun observeLiveData(lifecycleOwner: LifecycleOwner) {
        viewModel.state.observe(lifecycleOwner, Observer {
            when (it) {
                is ManagerScreenState.Loading -> Unit
                is ManagerScreenState.Render -> processResult(it.renderState)
            }
        })
    }

    private fun processResult(renderState: UIExchangeState) {
        when (renderState) {
            is UIExchangeState.ExchangeError -> Unit
            is UIExchangeState.ExchangeSuccessFrom -> {
                txtGo.clearFocus()
                txtGo.setText(renderState.result.toString())
                mountExchange = txtSend.text.toString().toFloat()
                resultReference = txtGo.text.toString().toFloat()
            }
            is UIExchangeState.ExchangeSuccessTo -> {
                txtSend.clearFocus()
                txtSend.setText(renderState.result.toString())
                mountExchange = txtSend.text.toString().toFloat()
                resultReference = txtGo.text.toString().toFloat()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GET_COUNTRY && resultCode == Activity.RESULT_OK) {

            val country: Country = data?.getSerializableExtra("country") as Country
            val type: String = data.getStringExtra("exchange") as String
            country.let {
                if (type == "from") {
                    btnSend.text = it.country
                    exchangeCurrencyFrom = it.money
                    viewModel.getExchangeFromaTo(
                        txtSend.text.toString().toFloat(),
                        exchangeCurrencyFrom,
                        exchangeCurrencyTo,
                        exchangeType!!.countries
                    )
                } else {
                    btnGo.text = it.country
                    exchangeCurrencyTo = it.money
                    viewModel.getExchangeToaFrom(
                        txtGo.text.toString().toFloat(),
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

    fun onLifecycleOwnerAttached(lifecycleOwner: LifecycleOwner) {
        observeLiveData(lifecycleOwner)
    }

}


