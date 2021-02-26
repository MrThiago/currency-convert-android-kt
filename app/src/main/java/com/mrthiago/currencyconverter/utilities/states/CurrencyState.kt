package com.mrthiago.currencyconverter.utilities.states

import com.mrthiago.currencyconverter.data.models.CurrencyData

sealed class CurrencyState {
    class Success(val resultData: List<CurrencyData>): CurrencyState()
    class Failure(val errorMessage: String): CurrencyState()
    class NoData(val noDataMessage: String): CurrencyState()
    object Empty: CurrencyState()
}