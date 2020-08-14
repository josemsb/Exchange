package com.bcptest.exchange.ui.component.entity

import java.io.Serializable

data class ExchangeType(
    val countries: List<Country>
): Serializable