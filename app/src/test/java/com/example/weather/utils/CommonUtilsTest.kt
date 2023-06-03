package com.example.weather.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.res.AssetManager
import com.example.weather.R
import com.example.weather.enums.TemperatureUnitType
import com.google.android.gms.location.FusedLocationProviderClient
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import java.io.InputStream

class CommonUtilsTest {
    @Mock
    private var mockContext: Context? = null
    @Mock
    private var mockSharedPreferences: SharedPreferences? = null
    @Mock
    private var mockEditor: Editor? = null
    @Mock
    private var mockLocationProvider: FusedLocationProviderClient? = null

    @Before
    fun setUp() {
        mockContext = mock(Context::class.java)
        mockSharedPreferences = mock(SharedPreferences::class.java)
        mockEditor = mock(Editor::class.java)
        mockLocationProvider = mock(FusedLocationProviderClient::class.java)
        Mockito.`when`(mockContext?.getString(R.string.degree)).thenReturn("Degree")
        Mockito.`when`(mockContext?.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)
        Mockito.`when`(mockSharedPreferences?.edit()).thenReturn(mockEditor)
    }

    @Ignore
    @Test
    fun loadCitiesTest() {
        val mockAssetManager = mock(AssetManager::class.java)
        val mockInputStream = mock(InputStream::class.java)
        Mockito.`when`(mockContext?.assets).thenReturn(mockAssetManager)
        Mockito.`when`(mockContext?.assets?.open("cities.txt")).thenReturn(mockInputStream)
        Mockito.`when`(mockInputStream.readBytes()).thenReturn(ByteArray(20))
        assertTrue(CommonUtils.loadCities(mockContext!!).size > 0)
    }

    @Test
    fun fetchCityTest() {
        CommonUtils.saveCity(mockContext!!, "Newark")
        assertEquals(CommonUtils.fetchCity(mockContext!!), "Newark")
    }

    @Test
    fun fetchTempUnit() {
        CommonUtils.saveTempUnit(mockContext!!, unitType = TemperatureUnitType.Fahrenheit)
        assertEquals(CommonUtils.fetchTempUnit(mockContext!!), TemperatureUnitType.Degree.toString())
    }

    @Test
    fun weatherIconTest() {
        assertEquals(CommonUtils.weatherIconUrl("10n"), "https://openweathermap.org/img/wn/10n@2x.png")
    }

    @After
    fun tearDown() {
        mockContext = null
        mockSharedPreferences = null
        mockEditor = null
        mockSharedPreferences = null
    }
}