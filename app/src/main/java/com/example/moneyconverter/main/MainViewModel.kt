package com.example.moneyconverter.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyconverter.data.models.Rates
import com.example.moneyconverter.util.DispatcherProvider
import com.example.moneyconverter.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.round

class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {


    sealed class MoneyEvent {
        class Success(val resultText: String) : MoneyEvent()
        class Failure(val errorText: String) : MoneyEvent()
        object Loading : MoneyEvent()
        object Empty : MoneyEvent()

    }

    private val _conversion = MutableStateFlow<MoneyEvent>(MoneyEvent.Empty)
    val conversion: StateFlow<MoneyEvent> = _conversion

    fun convert(
        amountStr: String,
        fromCurrency: String,
        toCurrency: String
    ) {
        val fromAmount = amountStr.toFloatOrNull()
        if (fromAmount == null) {
            _conversion.value = MoneyEvent.Failure("This is not a valid amount")

            return
        }

        viewModelScope.launch(dispatchers.io) {
            _conversion.value = MoneyEvent.Loading
            when (val ratesResponse = repository.getRates(fromCurrency)) {
                is Resource.Error -> _conversion.value = MoneyEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.rates
                    val rate = getRateForCurrency(toCurrency, rates)
                    if (rate == null) {
                        _conversion.value = MoneyEvent.Failure("Unexpected error")
                    } else {
                        val convertedCurrency = round(fromAmount * rate * 100) / 100
                        _conversion.value = MoneyEvent.Success(
                            "$fromAmount $fromCurrency = $convertedCurrency $toCurrency"
                        )
                    }
                }
            }
        }
    }

    private fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
        "CAD" -> rates.cAD
        "HKD" -> rates.hKD
        "ISK" -> rates.iSK
        "EUR" -> rates.eUR
        "PHP" -> rates.pHP
        "DKK" -> rates.dKK
        "HUF" -> rates.hUF
        "CZK" -> rates.cZK
        "AUD" -> rates.aUD
        "RON" -> rates.rON
        "SEK" -> rates.sEK
        "IDR" -> rates.iDR
        "INR" -> rates.iNR
        "BRL" -> rates.bRL
        "RUB" -> rates.rUB
        "HRK" -> rates.hRK
        "JPY" -> rates.jPY
        "THB" -> rates.tHB
        "CHF" -> rates.cHF
        "SGD" -> rates.sGD
        "PLN" -> rates.pLN
        "BGN" -> rates.bGN
        "CNY" -> rates.cNY
        "NOK" -> rates.nOK
        "NZD" -> rates.nZD
        "ZAR" -> rates.zAR
        "USD" -> rates.uSD
        "MXN" -> rates.mXN
        "ILS" -> rates.iLS
        "GBP" -> rates.gBP
        "KRW" -> rates.kRW
        //"MYR" -> rates.mYR
        else -> null
    }

}


