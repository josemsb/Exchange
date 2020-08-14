package com.bcptest.exchange.ui.component.shared

import android.content.Context
import com.bcptest.exchange.ui.component.shared.Parameters.AMOUNT_FROM
import com.bcptest.exchange.ui.component.shared.Parameters.AMOUNT_TO
import com.bcptest.exchange.ui.component.shared.Parameters.CURRENCY_FROM
import com.bcptest.exchange.ui.component.shared.Parameters.CURRENCY_TO
import com.bcptest.exchange.ui.component.shared.Parameters.STORAGE

class SharedStorage(private val context: Context) {

    var amountFrom: Float
        get() = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)
            .getFloat(AMOUNT_FROM, 0.0f)
        set(value) = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)
            .edit().putFloat(AMOUNT_FROM, value).apply()

    var amountTo: Float
        get() = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)
            .getFloat(AMOUNT_TO, 0.0f)
        set(value) = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)
            .edit().putFloat(AMOUNT_TO, value).apply()

    var currencyFrom: String
        get() = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)
            .getString(CURRENCY_FROM, "")!!
        set(value) = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)
            .edit().putString(CURRENCY_FROM, value).apply()

    var currencyTo: String
        get() = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)
            .getString(CURRENCY_TO, "")!!
        set(value) = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)
            .edit().putString(CURRENCY_TO, value).apply()
}