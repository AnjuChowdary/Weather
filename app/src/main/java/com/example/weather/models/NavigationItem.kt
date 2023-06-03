package com.example.weather.models

import com.example.weather.enums.NavigationItemType

data class NavigationItem(
    val name: String,
    val route: String,
    val itemType: NavigationItemType
)