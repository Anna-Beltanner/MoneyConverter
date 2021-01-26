package com.example.moneyconverter.data.models

data class MoneyResponse(
    val base: String,
    val date: String,
    val rates: Rates
)