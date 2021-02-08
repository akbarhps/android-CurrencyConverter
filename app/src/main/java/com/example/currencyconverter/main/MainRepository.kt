package com.example.currencyconverter.main

import android.accounts.NetworkErrorException
import com.example.currencyconverter.data.CurrencyServices
import com.example.currencyconverter.data.models.CurrencyResponse
import com.example.currencyconverter.util.Resource
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val services: CurrencyServices
) : MainRepositoryServices {

    override suspend fun getRates(baseCurrency: String): Resource<CurrencyResponse> {
        return try {
            val response = services.getRates(baseCurrency)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                throw NetworkErrorException(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}