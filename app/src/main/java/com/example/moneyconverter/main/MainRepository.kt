package com.example.moneyconverter.main

import com.example.moneyconverter.data.models.MoneyResponse
import com.example.moneyconverter.util.Resource

interface MainRepository {

    suspend fun getRates(base: String): Resource<MoneyResponse>
}