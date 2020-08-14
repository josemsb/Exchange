package com.bcptest.exchange.ui.presenter.exchange

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.bcptest.exchange.R
import com.bcptest.exchange.data.ExchangeProcessImpl
import com.bcptest.exchange.domain.usecase.ProcessUseCase
import com.bcptest.exchange.ui.component.entity.ExchangeType
import com.bcptest.exchange.ui.component.util.ActivityResultObservableActivity
import com.bcptest.exchange.util.ManagerScreenState
import com.bcptest.exchange.util.ViewModelUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_exchange.*
import java.io.IOException

class ExchangeActivity : ActivityResultObservableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange)

        initUI()
        initListener()
    }

    private fun initListener() {
        btnProccess.setOnClickListener { exchangeViewModel.proccess(uiExchange.mountExchange, uiExchange.resultReference, uiExchange.exchangeCurrencyFrom, uiExchange.exchangeCurrencyTo) }
    }

    private fun initUI() {
        val gson = Gson()
        uiExchange.loadExchange(
            gson.fromJson(
                loadJSONAsset("exchangeType.json"),
                ExchangeType::class.java
            )
        )
        exchangeViewModel.state.observe(::getLifecycle, ::updateUI)
    }

    private fun updateUI(screenState: ManagerScreenState<ExchangeState>) {
        when (screenState) {
            is ManagerScreenState.Loading -> Unit
            is ManagerScreenState.Render -> processRenderUI(screenState.renderState)
        }
    }

    private fun processRenderUI(renderState: ExchangeState) {

        when (renderState) {

            is ExchangeState.Error -> Unit
            is ExchangeState.Processs -> dialog()
        }
    }

    private fun loadJSONAsset(jsonFile: String): String? {

        val json: String
        try {
            val inputStream = this.assets.open(jsonFile)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.use { it.read(buffer) }
            json = String(buffer)

        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }

        return json
    }

    private fun dialog() {

        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.requestWindowFeature(0)

        dialog.setContentView(R.layout.dialog)
        dialog.show()


        val retryButton = dialog.findViewById(R.id.btnRetryDialog) as Button
        retryButton.setOnClickListener {
            dialog.dismiss()
        }

        val dismissButton = dialog.findViewById(R.id.btnDismissDialog) as Button
        dismissButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    private val exchangeViewModel by lazy {
        ViewModelProvider(
            this@ExchangeActivity,
            ViewModelUtil.viewModelFactory { createViewModel() }).get(ExchangeViewModel::class.java)
    }

    private fun createViewModel(): ExchangeViewModel {

        val exchangeProcess = ExchangeProcessImpl()
        val processUseCase = ProcessUseCase(exchangeProcess)

        return ExchangeViewModel(
            processUseCase
        )
    }

}