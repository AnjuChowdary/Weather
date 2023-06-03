package com.example.weather.extensions

import android.content.Context
import android.content.SharedPreferences
import com.example.weather.R
import com.example.weather.enums.TemperatureUnitType
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.util.TimeZone


class WeatherExtensionsKtTest {
    @Mock
    private var mockContext: Context? = null
    @Mock
    private var mockSharedPreferences: SharedPreferences? = null

    @Before
    fun setUp() {
        mockContext = Mockito.mock(Context::class.java)
        mockSharedPreferences = Mockito.mock(SharedPreferences::class.java)
        Mockito.`when`(mockContext?.getString(R.string.degree)).thenReturn("Degree")
    }

    @Test
    fun kelvinToDegreeCelsius() {
        val kelvinValue = 278.0
        assertSame(kelvinValue.kelvinToDegreeCelsius(), 4)
    }

    @Test
    fun kelvinToFahrenheit() {
        val kelvinValue = 278.0
        assertSame(kelvinValue.kelvinToFahrenheit(), 40)
    }

    @Test
    fun convertedValue() {
        val kelvinValue = 278.0
        Mockito.`when`(mockContext?.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)
        Mockito.`when`(mockSharedPreferences?.getString("unit", "unit")).thenReturn(TemperatureUnitType.Fahrenheit.toString())
        assertEquals(kelvinValue.convertedValue(mockContext!!), "4Degree")
    }

    @Test
    fun firstWord() {
        val city = "New York"
        assertEquals(city.firstWord(), "New")
    }

    @Test
    fun capitalize() {
        val city = "new york"
        assertEquals(city.capitalize(), "New York")
    }

    @Test
    fun toDate() {
        val today = 1685620450L
        assertEquals(today.toDate("HH:mm 'Z'", TimeZone.getTimeZone("UTC")), "4:54 ")
    }

    @After
    fun tearDown() {
        mockContext = null
        mockSharedPreferences = null
    }
}