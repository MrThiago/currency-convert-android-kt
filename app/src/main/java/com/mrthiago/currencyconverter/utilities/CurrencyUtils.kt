package com.mrthiago.currencyconverter.utilities

import com.google.gson.Gson
import com.mrthiago.currencyconverter.R
import com.mrthiago.currencyconverter.data.CurrencyRateResponse
import com.mrthiago.currencyconverter.data.models.CurrencyData
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.round

object CurrencyUtils {

    /***
     * Will Parse the Server Response, update the rate based on the user input and return a List
     */
    fun parseCurrencyRates(data: CurrencyRateResponse, forAmount: Float, fromBaseCurrency: String): MutableList<CurrencyData> {
        val currencyDataList = mutableListOf<CurrencyData>()
        val jsonObject = JSONObject(Gson().toJson(data))
        val currencyObj = jsonObject.getJSONObject("rates")

        val iter = currencyObj.keys()
        while (iter.hasNext()) {
            val key = iter.next()
            try {
                val value = currencyObj[key]
                val rate: Double = value.toString().toDouble()
                val convertedCurrency = round(forAmount * rate * 100) / 100
                val isRequestedBase: Boolean = (fromBaseCurrency == key)

                if (isRequestedBase) {
                    // If is the base currency requested, add it to the top of the list
                    currencyDataList.add(
                        0,
                        CurrencyData(
                            true,
                            key,
                            getCurrencyNameFromCode(key),
                            round(forAmount.toDouble()),
                            getCurrencyFlagFromCode(key)
                        )
                    )
                } else {
                    currencyDataList.add(
                        CurrencyData(
                            false,
                            key,
                            getCurrencyNameFromCode(key),
                            convertedCurrency,
                            getCurrencyFlagFromCode(key)
                        )
                    )
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        return currencyDataList
    }

    private fun getCurrencyNameFromCode(code: String): String {
        return when (code) {
            "AUD" -> "Australian Dollar"
            "BGN" -> "Bulgarian Lev"
            "BRL" -> "Brazilian Real"
            "CAD" -> "Canadian Dollar"
            "CHF" -> "Swiss Franc"
            "CNY" -> "Yuan Renminbi"
            "CZK" -> "Czech Koruna"
            "DKK" -> "Danish Krone"
            "GBP" -> "Pound Sterling"
            "HKD" -> "Hong Kong Dollar"
            "HRK" -> "Kuna"
            "HUF" -> "Forint"
            "IDR" -> "Rupiah"
            "ILS" -> "New Israeli Sheqel"
            "INR" -> "Indian Rupee"
            "ISK" -> "Iceland Krona"
            "JPY" -> "Yen"
            "KRW" -> "Won"
            "MXN" -> "Mexican Peso"
            "MYR" -> "Malaysian Ringgit"
            "NOK" -> "Norwegian Krone"
            "NZD" -> "New Zealand Dollar"
            "PHP" -> "Philippine Peso"
            "PLN" -> "Zloty"
            "RON" -> "Romanian Leu"
            "RUB" -> "Russian Ruble"
            "SEK" -> "Swedish Krona"
            "SGD" -> "Singapore Dollar"
            "THB" -> "Baht"
            "USD" -> "US Dollar"
            "ZAR" -> "Rand"
            "EUR" -> "Euro"
            else -> ""
        }
    }

    private fun getCurrencyFlagFromCode(code: String) =
        when (code) {
            "AUD" -> R.drawable.ic_aud
            "BGN" -> R.drawable.ic_bgn
            "BRL" -> R.drawable.ic_brl
            "CAD" -> R.drawable.ic_cad
            "CHF" -> R.drawable.ic_chf
            "CNY" -> R.drawable.ic_cny
            "CZK" -> R.drawable.ic_czk
            "DKK" -> R.drawable.ic_dkk
            "GBP" -> R.drawable.ic_gbp
            "HKD" -> R.drawable.ic_hkd
            "HRK" -> R.drawable.ic_hrk
            "HUF" -> R.drawable.ic_huf
            "IDR" -> R.drawable.ic_idr
            "ILS" -> R.drawable.ic_ils
            "INR" -> R.drawable.ic_inr
            "ISK" -> R.drawable.ic_isk
            "JPY" -> R.drawable.ic_jpy
            "KRW" -> R.drawable.ic_krw
            "MXN" -> R.drawable.ic_mxn
            "MYR" -> R.drawable.ic_myr
            "NOK" -> R.drawable.ic_nok
            "NZD" -> R.drawable.ic_nzd
            "PHP" -> R.drawable.ic_php
            "PLN" -> R.drawable.ic_pln
            "RON" -> R.drawable.ic_ron
            "RUB" -> R.drawable.ic_rub
            "SEK" -> R.drawable.ic_sek
            "SGD" -> R.drawable.ic_sgd
            "THB" -> R.drawable.ic_thb
            "ZAR" -> R.drawable.ic_zar
            "EUR" -> R.drawable.ic_eur
            "USD" -> R.drawable.ic_usd
            else -> R.drawable.ic_empty
        }
}