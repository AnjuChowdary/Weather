package com.example.weather.utils

import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.weather.enums.NavigationItemType
import com.example.weather.enums.TemperatureUnitType
import com.example.weather.models.LocationLatAndLon
import com.example.weather.models.NavigationItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.Locale

class CommonUtils {
    companion object {
        private const val prefName = "PREFERENCE"
        private var sharedPref: SharedPreferences? = null
        val navigationItems = listOf(
            NavigationItem(name = "", "settings", NavigationItemType.Settings),
            NavigationItem(name = "Current Location", "home", NavigationItemType.Home),
            NavigationItem(name = "Other Locations", "places", NavigationItemType.AddLocation),
            NavigationItem(name = "Temperature Units", "home", NavigationItemType.TempUnit)
        )

        fun loadCities(context: Context): ArrayList<String> {
            val values = ArrayList<String>()
            InputStreamReader(context.assets.open("cities.txt")).let { inputStream ->
                BufferedReader(inputStream).let {bufferedReader ->
                    var line = bufferedReader.readLine()
                    while (line != null) {
                        values.add(line)
                        line = bufferedReader.readLine()
                    }
                }
            }
            return  values
        }

        fun saveCity(context: Context, city: String) {
            if(sharedPref == null) {
                sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            }
            val editor = sharedPref!!.edit()
            editor.putString("city", city)
            editor.apply()
        }

        fun fetchCity(context: Context): String {
            if (sharedPref == null) {
                sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            }
            return (sharedPref!!.getString("city", "Newark") ?: "Newark").trim()
        }

        fun saveTempUnit(context: Context, unitType: TemperatureUnitType) {
            if(sharedPref == null) {
                sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            }
            val editor = sharedPref!!.edit()
            editor.putString("unit", unitType.toString())
            editor.apply()
        }

        fun fetchTempUnit(context: Context): String {
            if (sharedPref == null) {
                sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            }
            return sharedPref!!.getString("unit", TemperatureUnitType.Degree.toString()) ?: TemperatureUnitType.Degree.toString()
        }

        fun weatherIconUrl(code: String) =
            "https://openweathermap.org/img/wn/$code@2x.png"

    }
}