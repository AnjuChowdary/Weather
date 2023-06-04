package com.example.weather.viewmodels

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.datautil.NetworkResponse
import com.example.weather.data.models.WeatherResponse
import com.example.weather.data.repository.DataRepository
import com.example.weather.enums.TemperatureUnitType
import com.example.weather.extensions.convertedValue
import com.example.weather.utils.CommonUtils
import com.example.weather.models.TemperatureData
import com.example.weather.utils.LocationUtils
import kotlinx.coroutines.launch

class WeatherViewModel(private val mRepository: DataRepository): ViewModel(), DefaultLifecycleObserver {
    private val _mWeatherResponse: MutableState<NetworkResponse<WeatherResponse?>> =
        mutableStateOf(NetworkResponse.Idle())
    val mWeatherResponse: State<NetworkResponse<WeatherResponse?>> = _mWeatherResponse

    private val _mLocality: MutableState<String> =
        mutableStateOf("")
    val mLocality: State<String> = _mLocality

    private val _mTempData: MutableState<TemperatureData> =
        mutableStateOf(TemperatureData(temperature = "45", minimumTemperature = "20", maximumTemperature = "50", feelsLike = "20"))
    val mTempData: State<TemperatureData> = _mTempData

    val showLoader: MutableState<Boolean> = mutableStateOf(true)

    fun fetchWeatherForCity(context: Context, city: String) {
        _mWeatherResponse.value = NetworkResponse.Loading()
        viewModelScope.launch {
            mRepository.fetchWeather(city).let {
                _mWeatherResponse.value = it
                _mLocality.value = _mWeatherResponse.value.response?.name ?: "Newark"
                _mTempData.value = TemperatureData(
                    temperature = _mWeatherResponse.value.response?.main?.temp_min?.convertedValue(context = context) ?: "",
                    minimumTemperature = _mWeatherResponse.value.response?.main?.temp_min?.convertedValue(context = context) ?: "",
                    maximumTemperature = _mWeatherResponse.value.response?.main?.temp_min?.convertedValue(context = context) ?: "",
                    feelsLike = _mWeatherResponse.value.response?.main?.feels_like?.convertedValue(context = context) ?: "")
            }
        }
    }

    fun getLocality(context: Context) {
        _mLocality.value = ""
        LocationUtils.getUserLocation(context) {
            val locality = LocationUtils.getCurrentLocality(it, context = context)
            _mLocality.value = locality
            CommonUtils.saveCity(context = context, locality)
        }
    }

    fun convertTempUnit(context: Context, unitType: TemperatureUnitType) {
        CommonUtils.saveTempUnit(context = context, unitType = unitType)
        _mTempData.value = TemperatureData(
            temperature = _mWeatherResponse.value.response?.main?.temp_min?.convertedValue(context = context) ?: "",
            minimumTemperature = _mWeatherResponse.value.response?.main?.temp_min?.convertedValue(context = context) ?: "",
            maximumTemperature = _mWeatherResponse.value.response?.main?.temp_min?.convertedValue(context = context) ?: "",
            feelsLike = _mWeatherResponse.value.response?.main?.feels_like?.convertedValue(context = context) ?: "")
    }
}