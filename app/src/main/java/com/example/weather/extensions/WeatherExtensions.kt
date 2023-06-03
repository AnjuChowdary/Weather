package com.example.weather.extensions

import android.content.Context
import com.example.weather.R
import com.example.weather.enums.TemperatureUnitType
import com.example.weather.utils.CommonUtils
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Locale
import java.util.TimeZone


fun Double.kelvinToDegreeCelsius(): Int {
    return (this - 273.15).toInt()
}

fun Double.kelvinToFahrenheit(): Int {
    return ((this - 273.15)*9/5 + 32).toInt()
}

fun Double.convertedValue(context: Context): String {
    when(CommonUtils.fetchTempUnit(context)) {
        TemperatureUnitType.Degree.toString() -> {
            return this.kelvinToDegreeCelsius().toString() + context.getString(R.string.degree)
        }
        TemperatureUnitType.Fahrenheit.toString() -> {
            return this.kelvinToFahrenheit().toString()
        }
    }
    return this.kelvinToDegreeCelsius().toString() + context.getString(R.string.degree)
}

fun String.firstWord(): String {
    return this.split(" ").first()
}

fun String.capitalize(): String {
    var output = ""
    this.split(" ").toMutableList().let {words->
        for(word in words){
            output += word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } +" "
        }
    }
    return output.trim()
}

fun Long.toDate(dateFormat: String = "HH:mm 'Z'", timeZone: TimeZone = TimeZone.getTimeZone("UTC")): String {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    val time = Instant.ofEpochSecond(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
    parser.timeZone = timeZone
    return time.hour.toString()+":"+time.minute.toString()+" "
}