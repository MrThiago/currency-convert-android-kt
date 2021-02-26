package com.mrthiago.currencyconverter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrthiago.currencyconverter.base.Constants.Companion.API_CALL_DELAY
import com.mrthiago.currencyconverter.repository.CurrencyRepository
import com.mrthiago.currencyconverter.utilities.CurrencyUtils.parseCurrencyRates
import com.mrthiago.currencyconverter.utilities.states.CurrencyState
import com.mrthiago.currencyconverter.utilities.states.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: CurrencyRepository) : ViewModel() {

    private val _currencyRates = MutableStateFlow<CurrencyState>(CurrencyState.Empty)
    val currencyRates: StateFlow<CurrencyState> = _currencyRates

    // Currency Base and Amount to convert
    private lateinit var currencyBase: String
    private var amountToConvert: Int = 0

    private val dispatcherIO = Dispatchers.IO
    private var job: Job? = null


    fun getRates(fromBaseCurrency: String = "EUR", forAmount: Int = 1) {
        // Store Values
        currencyBase = fromBaseCurrency
        amountToConvert = forAmount

        stopUpdates()
        job = initiateApiCalls()
    }

    private fun initiateApiCalls(): Job {
        return viewModelScope.launch(dispatcherIO) {
            // Repeat as long isActive = true AND every API_CALL_DELAY interval
            while (isActive) {
                when (val response = repository.getRatesFromNetwork(currencyBase)) {
                    is NetworkState.Success -> {
                        // Process the Response and map to CurrencyData model and return the data as a List of CurrencyData
                        val newList = parseCurrencyRates(response.data, amountToConvert.toFloat(), currencyBase)
                        if (newList.isEmpty()) {
                            _currencyRates.value = CurrencyState.NoData("Currency Rates not available at the moment")
                        } else {
                            _currencyRates.value = CurrencyState.Success(newList)
                        }
                    }
                    is NetworkState.Error -> {
                        _currencyRates.value = CurrencyState.Failure(response.error)
                    }
                }

                delay(API_CALL_DELAY)
            }
        }
    }

    fun stopUpdates() {
        job?.cancel()
        job = null
    }
}