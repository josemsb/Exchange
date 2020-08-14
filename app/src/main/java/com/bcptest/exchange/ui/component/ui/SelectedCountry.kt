package com.bcptest.exchange.ui.component.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bcptest.exchange.R
import com.bcptest.exchange.ui.component.entity.Country
import com.bcptest.exchange.ui.component.entity.ExchangeType
import kotlinx.android.synthetic.main.activity_seleted_country.*

private const val EXCHANGE = "exchange"
private const val EXCHANGETYPEe = "exchangeType"
private const val COUNTRY = "country"

class SelectedCountry : AppCompatActivity() {

    private var selectedCountryAdapter: SelectedCountryAdapter? = null
    var exchange: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleted_country)

        initUI()
        initData(intent.extras)
    }

    private fun initUI() {
        toolbar.setNavigationOnClickListener {
            val returnIntent = Intent()
            setResult(Activity.RESULT_CANCELED, returnIntent)
            finish()
        }
    }

    //Load data to recyclerView
    private fun initData(extras: Bundle?) {
        if (extras != null) {
            exchange = extras.getString(EXCHANGE) as String
            val exchangeType: ExchangeType = extras.getSerializable(EXCHANGETYPEe) as ExchangeType
            if (exchangeType.countries.isNotEmpty())
                loadData(exchangeType.countries)
            else {
                val returnIntent = Intent()
                setResult(Activity.RESULT_CANCELED, returnIntent)
                finish()
            }
        }
    }

    private fun loadData(countries: List<Country>) {
        rvCountries.layoutManager = LinearLayoutManager(this)
        selectedCountryAdapter =
            SelectedCountryAdapter(
                countries
            )

        selectedCountryAdapter?.setOnClickItem(object : OnClickItem {
            override fun onClickItem(country: Country) {
                val returnIntent = Intent()
                returnIntent.putExtra(COUNTRY, country)
                returnIntent.putExtra(EXCHANGE, exchange)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }

        })

        rvCountries.adapter = selectedCountryAdapter
    }
}