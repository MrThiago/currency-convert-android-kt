package com.mrthiago.currencyconverter.repository

import android.content.Context
import android.text.TextUtils
import com.mrthiago.currencyconverter.R
import com.mrthiago.currencyconverter.api.CurrencyService
import com.mrthiago.currencyconverter.data.CurrencyRateResponse
import com.mrthiago.currencyconverter.utilities.states.NetworkState
import javax.inject.Inject

class CurrencyRepository @Inject constructor(private val api: CurrencyService, private val context: Context) {

    suspend fun getRatesFromNetwork(base: String): NetworkState<CurrencyRateResponse> {
        NetworkState.Loading
        return try {
            val response = api.getCurrency(base)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                NetworkState.Success(result)
            } else {
                if (!TextUtils.isEmpty(response.message())) {
                    NetworkState.Error(response.message())
                } else {
                    NetworkState.Error(context.getString(R.string.error_message))
                }
            }
        } catch (e: Exception) {
            val message = e.message ?: "Error"

            if (message.contains("Unable to resolve host")) {
                NetworkState.Error(context.getString(R.string.network_error))
            } else {
                NetworkState.Error("")
            }
        }
    }
}