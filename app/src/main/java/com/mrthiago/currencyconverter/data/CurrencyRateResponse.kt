package com.mrthiago.currencyconverter.data

data class CurrencyRateResponse(
    val baseCurrency: String,
    val rates: Rates
)