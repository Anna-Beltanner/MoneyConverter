package com.example.moneyconverter.di

import android.app.Application
import com.example.moneyconverter.data.MoneyApi
import com.example.moneyconverter.main.DefaultMainRepository
import com.example.moneyconverter.main.MainRepository
import com.example.moneyconverter.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

private const val BASE_URL = "https://api.exchangeratesapi.io/"

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCurrencyApi(): MoneyApi = Retrofit.Builder()

        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MoneyApi::class.java)

    @Singleton
    @Provides
    fun provideMainRepository(api:MoneyApi): MainRepository = DefaultMainRepository(api)

    @Singleton
    @Provides
    fun provideDispatchers():DispatcherProvider = object : DispatcherProvider{

        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined

    }




}