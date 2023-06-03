package com.example.weather.data.service

import com.example.weather.data.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IServiceRequest {
    @GET("weather?")
    suspend fun fetchWeather(@Query("q") city: String, @Query("appid") apiKey: String): Response<WeatherResponse>
}