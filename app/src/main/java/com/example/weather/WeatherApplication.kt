package com.example.weather

import android.app.Application
import com.example.weather.di.weatherModule
import org.koin.core.context.startKoin

class WeatherApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(weatherModule)
        }
    }
}