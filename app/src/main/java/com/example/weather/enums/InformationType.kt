package com.example.weather.enums

enum class InformationType {
    Humidity {
        override fun toString(): String {
            return "Humidity"
        }
             },
    MinTemp {
        override fun toString(): String {
            return "Min Temperature"
        }
            },
    MaxTemp {
        override fun toString(): String {
            return "Max Temperature"
        }
            },
    WindSpeed {
        override fun toString(): String {
            return "Wind Speed"
        }
              },
    Sunrise {
        override fun toString(): String {
            return "Sunrise"
        }
    },
    Sunset {
        override fun toString(): String {
            return "Sunset"
        }
    }
}