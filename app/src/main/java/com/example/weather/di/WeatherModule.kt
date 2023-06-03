package com.example.weather.di

import com.example.weather.data.repository.DataRepository
import com.example.weather.data.repository.NetworkingService
import com.example.weather.data.service.IServiceRequest
import com.example.weather.viewmodels.WeatherViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val NETWORK_REQUEST_TIMEOUT_SECONDS = 15L
const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

val weatherModule = module {
    factory { provideRetrofit() }
    single { NetworkingService(get()) }
    single { DataRepository(get()) }
    viewModel { WeatherViewModel(get()) }
}

fun provideRetrofit(): IServiceRequest =
    Retrofit.Builder()
        .client(getOkHttpClient())
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(IServiceRequest::class.java)

private fun getOkHttpClient() =
    OkHttpClient.Builder()
        .connectTimeout(NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()