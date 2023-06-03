package com.example.weather.enums

sealed class Screens(val route: String) {
    object Launch: Screens("launch")
    object Home: Screens("home")
    object Places: Screens("places")
}