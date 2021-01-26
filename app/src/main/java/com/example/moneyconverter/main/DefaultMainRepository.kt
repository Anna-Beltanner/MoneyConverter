package com.example.moneyconverter.main

import com.example.moneyconverter.data.MoneyApi
import com.example.moneyconverter.data.models.MoneyResponse
import com.example.moneyconverter.util.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: MoneyApi
) : MainRepository {

    override suspend fun getRates(base: String): Resource<MoneyResponse> {
        return try {
            val response = api.getRates(base)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occured")
        }
    }
}