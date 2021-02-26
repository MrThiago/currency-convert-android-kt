package com.mrthiago.currencyconverter.api

import com.mrthiago.currencyconverter.data.CurrencyRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {

    @GET("/api/android/latest")
    suspend fun getCurrency(@Query("base") baseCurrency: String = "EUR"): Response<CurrencyRateResponse>
}