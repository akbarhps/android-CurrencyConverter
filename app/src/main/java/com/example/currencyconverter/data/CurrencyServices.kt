package com.example.currencyconverter.data

import com.example.currencyconverter.data.models.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyServices {

    @GET("/latest")
    suspend fun getRates(
        @Query("base") baseCurrency: String
    ): Response<CurrencyResponse>

}