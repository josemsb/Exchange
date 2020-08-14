package com.bcptest.exchange.ui.component.entity

import java.io.Serializable

data class Country(
    val country: String,
    val image: String,
    val money: String,
    val valor: Float,
    val selectedSend: Boolean,
    val selectedGo: Boolean
): Serializable