package com.example.weather.data.repository

import com.example.weather.data.BuildConfig
import com.example.weather.data.datautil.NetworkResponse
import com.example.weather.data.service.IServiceRequest
import retrofit2.Response

class NetworkingService(private val mIServiceRequest: IServiceRequest) {

    internal suspend fun fetchWeather(city: String) = wrapNetworkResponse(mIServiceRequest.fetchWeather(city, BuildConfig.CONSUMER_KEY))

    private fun <T> wrapNetworkResponse(networkCallResult: Response<T>) = networkCallResult.runCatching {
        if (this.isSuccessful) NetworkResponse.Success(data = this.body())
        else NetworkResponse.Error(message = this.message())
    }.getOrDefault(NetworkResponse.Error(message = "Something went wrong"))
}