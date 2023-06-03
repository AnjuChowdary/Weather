package com.example.weather.enums

enum class TemperatureUnitType {
    Fahrenheit {
        override fun toString(): String {
            return "Fahrenheit"
        }
    },
    Degree {
        override fun toString(): String {
            return "Degree"
        }
    }
}