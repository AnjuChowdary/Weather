package com.example.weather.data.models

data class WeatherResponse(
    val coord: Coordinates,
    val weather: ArrayList<Weather>,
    val base: String,
    val main: MainData,
    val visibility: Long,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: SystemData,
    val timezone: Long,
    val id: Long,
    val name: String,
    val code: Int
)

data class Coordinates(
    val lon: Double,
    val lat: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class MainData(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class Wind (
    val speed: Double,
    val deg: Double
)

data class Clouds(
    val all: Int
)

data class SystemData(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)