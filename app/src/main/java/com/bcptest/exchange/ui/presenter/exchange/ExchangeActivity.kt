package com.bcptest.exchange.ui.presenter.exchange

import android.os.Bundle
import com.bcptest.exchange.R
import com.bcptest.exchange.ui.component.util.ActivityResultObservableActivity
import com.bcptest.exchange.ui.component.entity.ExchangeType
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_exchange.*
import java.io.IOException

class ExchangeActivity : ActivityResultObservableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange)

        initUI()

    }

    private fun initUI() {
        val gson = Gson()
        uiExchange.loadExchange(
            gson.fromJson(
                loadJSONAsset("exchangeType.json"),
                ExchangeType::class.java
            )
        )
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

}