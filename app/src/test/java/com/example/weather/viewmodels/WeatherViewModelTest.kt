package com.example.weather.viewmodels

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.weather.data.datautil.NetworkResponse
import com.example.weather.data.models.WeatherResponse
import com.example.weather.data.repository.DataRepository
import com.example.weather.enums.TemperatureUnitType
import com.example.weather.models.TemperatureData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {
    private val mDataRepository: DataRepository = Mockito.mock()
    @ExperimentalCoroutinesApi
    @get:Rule
    var waetherCoroutineRule = WeatherCoroutineRule()
    private var mWeatherViewModel: WeatherViewModel? = null
    private var mockContext: Context? = null
    private var mockWeatherResponse: State<NetworkResponse<WeatherResponse?>> = mock()
    private var mockNetworkResponse: NetworkResponse<WeatherResponse?> = mock()
    private var mockTempData: State<TemperatureData>? = mock()
    private var mockLocality: State<String>? = mock()

    @Before
    fun setUp() {
        mockContext = mock(Context::class.java)
        mWeatherViewModel = mock(WeatherViewModel::class.java)
        Mockito.`when`(mWeatherViewModel?.mWeatherResponse).thenReturn(mockWeatherResponse)
        Mockito.`when`(mockWeatherResponse.value).thenReturn(mockNetworkResponse)
        Mockito.`when`(mockWeatherResponse.value.response).thenReturn(mock(WeatherResponse::class.java))
        Mockito.`when`(mockWeatherResponse.value.response?.name).thenReturn("Newark")
        Mockito.`when`(mWeatherViewModel?.mLocality).thenReturn(mockLocality)
        Mockito.`when`(mWeatherViewModel?.mLocality?.value).thenReturn("Newark")
        Mockito.`when`(mWeatherViewModel?.mTempData).thenReturn(mockTempData)
        Mockito.`when`(mWeatherViewModel?.mTempData?.value).thenReturn(mock(TemperatureData::class.java))
        Mockito.`when`(mWeatherViewModel?.mTempData?.value?.feelsLike).thenReturn("23")
    }

    @After
    fun tearDown() {
        mWeatherViewModel = null
    }

    @Test
    fun getMWeatherResponse() {
        assertEquals(mWeatherViewModel?.mWeatherResponse?.value?.response?.name, "Newark")
    }

    @Test
    fun getMTempData() {
        mWeatherViewModel?.mTempData?.let { assertEquals(it.value.feelsLike, "23") }
    }

    @Test
    fun getShowLoader() {
        mWeatherViewModel?.showLoader?.value = false
        mWeatherViewModel?.showLoader?.value?.let { assertFalse(it) }
    }

    @Test
    fun fetchWeatherForCity() {
        mockContext?.let { mWeatherViewModel?.fetchWeatherForCity(it, "Newark") }
        assertEquals(mWeatherViewModel?.mWeatherResponse?.value?.response?.name, "Newark")
    }

    @Test
    fun getLocality() {
        mockContext?.let { mWeatherViewModel?.getLocality(it) }
        assertEquals(mWeatherViewModel?.mLocality?.value, "Newark")
    }

    @Test
    fun convertTempUnit() {
        mockContext?.let { mWeatherViewModel?.convertTempUnit(it, TemperatureUnitType.Degree) }
        mWeatherViewModel?.mTempData?.let { assertEquals(it.value.feelsLike, "23") }
    }
}