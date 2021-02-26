package com.mrthiago.currencyconverter.utilities

import com.google.gson.Gson
import com.mrthiago.currencyconverter.data.CurrencyRateResponse
import com.mrthiago.currencyconverter.utilities.CurrencyUtils.parseCurrencyRates
import org.junit.Assert.*
import org.junit.Test


class CurrencyUtilsTest {

    @Test
    fun parsedCurrencyRatesList() {
        val response = "{\"baseCurrency\":\"EUR\",\"rates\":{\"AUD\":1.585,\"BGN\":1.981,\"BRL\":4.229,\"CAD\":1.499,\"CHF\":1.156,\"CNY\":7.761,\"CZK\":26.089,\"DKK\":7.55,\"GBP\":0.888,\"HKD\":8.971,\"HRK\":7.455,\"HUF\":322.732,\"IDR\":16044.066,\"ILS\":4.117,\"INR\":82.061,\"ISK\":134.426,\"JPY\":124.867,\"KRW\":1275.883,\"MXN\":21.742,\"MYR\":4.684,\"NOK\":9.942,\"NZD\":1.646,\"PHP\":59.915,\"PLN\":4.362,\"RON\":4.781,\"RUB\":75.95,\"SEK\":10.611,\"SGD\":1.542,\"THB\":35.981,\"USD\":1.136,\"ZAR\":16.136}}"
        val dataClass: CurrencyRateResponse = Gson().fromJson(response, CurrencyRateResponse::class.java)

        val result = parseCurrencyRates(dataClass, 1f, "EUR")

        assertNotNull(result)
    }
}