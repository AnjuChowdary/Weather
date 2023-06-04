package com.example.weather.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.State
import com.example.weather.data.datautil.NetworkResponse
import com.example.weather.data.models.WeatherResponse
import com.example.weather.data.repository.DataRepository
import com.example.weather.data.repository.NetworkingService
import com.example.weather.data.service.IServiceRequest
import com.example.weather.enums.TemperatureUnitType
import com.example.weather.models.TemperatureData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {
    private var mNetworkingService: NetworkingService? = null
    private val mIServiceRequest: IServiceRequest = mock()
    private var mDataRepository: DataRepository? = null
    @Mock
    private var mockSharedPreferences: SharedPreferences? = null
    @Mock
    private var mockEditor: SharedPreferences.Editor? = null
    @ExperimentalCoroutinesApi
    @get:Rule
    var weatherCoroutineRule = WeatherCoroutineRule()
    private var mWeatherViewModel: WeatherViewModel? = null
    private var mockContext: Context? = null
    private var mockWeatherResponse: State<NetworkResponse<WeatherResponse?>> = mock()
    private var mockNetworkResponse: NetworkResponse<WeatherResponse?> = mock()


    @Before
    fun setUp() {
        mockContext = mock(Context::class.java)
        mockSharedPreferences = mock(SharedPreferences::class.java)
        mockEditor = mock(SharedPreferences.Editor::class.java)
        mNetworkingService = NetworkingService(mIServiceRequest)
        mDataRepository = DataRepository(mNetworkingService!!)
        mWeatherViewModel = WeatherViewModel(mDataRepository!!)
        Mockito.`when`(mockContext?.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)
        Mockito.`when`(mockSharedPreferences?.edit()).thenReturn(mockEditor)
        Mockito.`when`(mockWeatherResponse.value).thenReturn(mockNetworkResponse)
        Mockito.`when`(mockWeatherResponse.value.response).thenReturn(mock(WeatherResponse::class.java))
        Mockito.`when`(mockWeatherResponse.value.response?.name).thenReturn("Newark")
    }

    @After
    fun tearDown() {
        mWeatherViewModel = null
    }

    @Test
    fun getMTempData() {
        mWeatherViewModel?.mTempData?.let { assertEquals(it.value.feelsLike, "20") }
    }

    @Test
    fun getShowLoader() {
        mWeatherViewModel?.showLoader?.value = false
        mWeatherViewModel?.showLoader?.value?.let { assertFalse(it) }
    }

    @Test
    fun fetchWeatherForCity() {
        runTest {
            mockContext?.let { mWeatherViewModel?.fetchWeatherForCity(it, "Newark") }
            assertNotNull(mWeatherViewModel?.mWeatherResponse?.value)
        }
    }

    @Test
    fun convertTempUnit() {
        mockContext?.let { mWeatherViewModel?.convertTempUnit(it, TemperatureUnitType.Degree) }
        assertNotNull(mWeatherViewModel?.mTempData?.value)
    }
}