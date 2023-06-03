package com.example.weather.data.repository

import com.example.weather.data.datautil.NetworkResponse
import com.example.weather.data.models.WeatherResponse

class DataRepository(private val mNetworkingService: NetworkingService) {

    suspend fun fetchWeather(city: String): NetworkResponse<WeatherResponse?> = runCatching {
        mNetworkingService.fetchWeather(city)
    }.getOrElse {
        NetworkResponse.Error(message = it.message)
    }
}