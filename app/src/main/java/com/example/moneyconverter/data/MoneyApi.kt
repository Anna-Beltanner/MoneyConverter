package com.example.moneyconverter.data

import com.example.moneyconverter.data.models.MoneyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoneyApi {

    @GET("/latest")

    suspend fun getRates(
        @Query("base") base:String): Response<MoneyResponse>
}