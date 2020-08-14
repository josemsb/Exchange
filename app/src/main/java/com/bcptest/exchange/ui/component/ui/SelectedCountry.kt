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

class SelectedCountry : AppCompatActivity() {

    private var selectedCountryAdapter: SelectedCountryAdapter? = null
    var exchange: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleted_country)

        initUI()
        initData(intent.extras)

    }

    private fun initData(extras: Bundle?) {
        if (extras != null) {
            exchange = extras.getString("exchange") as String
            val exchangeType: ExchangeType = extras.getSerializable("exchangeType") as ExchangeType
            if (exchangeType.countries.isNotEmpty())
                loadData(exchangeType.countries)
            else {
                val returnIntent = Intent()
                setResult(Activity.RESULT_CANCELED, returnIntent)
                finish()
            }
        }
    }

    private fun initUI() {
        toolbar.setNavigationOnClickListener {
            val returnIntent = Intent()
            setResult(Activity.RESULT_CANCELED, returnIntent)
            finish()
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
                returnIntent.putExtra("country", country)
                returnIntent.putExtra("exchange", exchange)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }

        })

        rvCountries.adapter = selectedCountryAdapter
    }
}