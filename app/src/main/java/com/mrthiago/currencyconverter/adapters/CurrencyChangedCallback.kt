package com.mrthiago.currencyconverter.adapters

interface CurrencyChangedCallback {

    fun onCurrencyChanged(code: String, amount: Int)
}