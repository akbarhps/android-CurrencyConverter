package com.example.currencyconverter.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.models.Rates
import com.example.currencyconverter.util.DispatcherProvider
import com.example.currencyconverter.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Math.round

class MainViewModel @ViewModelInject constructor(
    private val repositoryServices: MainRepositoryServices,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    sealed class CurrencyEvents {
        object Idle                             : CurrencyEvents()
        object Loading                          : CurrencyEvents()
        class Success(val resultText: String)   : CurrencyEvents()
        class Failure(val errorText: String)    : CurrencyEvents()
    }

    private val _uiState = MutableStateFlow<CurrencyEvents>(CurrencyEvents.Idle)
    val uiState: StateFlow<CurrencyEvents> = _uiState

    fun convertCurrency(
        convertAmount: String,
        fromCurrency: String,
        toCurrency: String
    ) {
        val fromAmount = convertAmount.toFloatOrNull()
        if (fromAmount == null) {
            _uiState.value = CurrencyEvents.Failure("Not a valid amount")
            return
        }

        _uiState.value = CurrencyEvents.Loading
        viewModelScope.launch(dispatcher.io) {
            when(val rateResponse = repositoryServices.getRates(fromCurrency)) {
                is Resource.Error -> _uiState.value = CurrencyEvents.Failure(rateResponse.errorMessage!!)
                is Resource.Success -> {
                    val rates = rateResponse.data!!.rates
                    val rate = getRateForCurrency(toCurrency, rates)
                    if (rate == null) {
                        _uiState.value = CurrencyEvents.Failure("Unexpected Error")
                    } else {
                        val convertedAmount = round(fromAmount * rate * 100) / 100
                        _uiState.value = CurrencyEvents.Success(
                            "$fromAmount $fromCurrency = $convertedAmount $toCurrency"
                        )
                    }
                }
            }
        }
    }

    private fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
        "CAD" -> rates.CAD
        "HKD" -> rates.HKD
        "ISK" -> rates.ISK
        "EUR" -> rates.EUR
        "PHP" -> rates.PHP
        "DKK" -> rates.DKK
        "HUF" -> rates.HUF
        "CZK" -> rates.CZK
        "AUD" -> rates.AUD
        "RON" -> rates.RON
        "SEK" -> rates.SEK
        "IDR" -> rates.IDR
        "INR" -> rates.INR
        "BRL" -> rates.BRL
        "RUB" -> rates.RUB
        "HRK" -> rates.HRK
        "JPY" -> rates.JPY
        "THB" -> rates.THB
        "CHF" -> rates.CHF
        "SGD" -> rates.SGD
        "PLN" -> rates.PLN
        "BGN" -> rates.BGN
        "CNY" -> rates.CNY
        "NOK" -> rates.NOK
        "NZD" -> rates.NZD
        "ZAR" -> rates.ZAR
        "USD" -> rates.USD
        "MXN" -> rates.MXN
        "ILS" -> rates.ILS
        "GBP" -> rates.GBP
        "KRW" -> rates.KRW
        "MYR" -> rates.MYR
        else -> null
    }

}