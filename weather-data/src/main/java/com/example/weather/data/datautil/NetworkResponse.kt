package com.example.weather.data.datautil

sealed class NetworkResponse<T>(val response: T? = null, val errorMessage: String? = null) {
    data class Success<T>(val data: T): NetworkResponse<T>(response = data)
    data class Error<T>(val message: String?, val data: T? = null): NetworkResponse<T>(response = data, errorMessage = message)
    class Loading<T>(): NetworkResponse<T>()
    class Idle<T>(): NetworkResponse<T>()
}
