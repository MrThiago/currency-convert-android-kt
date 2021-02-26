package com.mrthiago.currencyconverter.data.models

// Model for the List Data
data class CurrencyData(
        val isBase: Boolean,
        val code: String,
        val name: String,
        val amount: Double,
        val image: Int
)