package com.example.weather.commoncomposables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.NavController
import com.example.weather.R
import com.example.weather.enums.TemperatureUnitType
import com.example.weather.ui.theme.BlackLight
import com.example.weather.ui.theme.WeatherFontTypography
import com.example.weather.ui.theme.WeatherTheme
import com.example.weather.enums.Screens

@Composable
fun SettingsNavigationItem(action: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 30.dp, end = 30.dp, top = 30.dp), horizontalArrangement = Arrangement.End) {
        Icon(
            Icons.Filled.Settings, contentDescription = null, tint = Color.White, modifier = Modifier
                .width(30.dp)
                .height(30.dp)
                .clickable { action() })
    }
}

@Composable
fun CurrentLocationNavigationItem(locality: String, temp: String, action: () -> Unit) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Column(modifier = Modifier
        .padding(30.dp)
        .clickable { action() }) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(
                Icons.Filled.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier
                    .width(30.dp)
                    .height(30.dp))
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = stringResource(id = R.string.current_location), style = WeatherFontTypography.labelMedium, color = Color.White)
        }

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp, top = 10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                        .padding(end = 5.dp))
                Text(text = locality, color = Color.White, style = WeatherFontTypography.labelMedium)
            }

            Row(verticalAlignment = Alignment.Top) {
                Text(text = temp, color = Color.White, style = WeatherFontTypography.labelMedium)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Canvas(
            Modifier
                .fillMaxWidth()
                .height(2.dp)) {
            drawLine(
                color = Color.White,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                pathEffect = pathEffect,
                strokeWidth = 2f
            )
        }
    }
}


@Composable
fun AddOtherLocations(navController: NavController, action: () -> Unit) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Column(modifier = Modifier
        .padding(start = 30.dp, end = 30.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
                    )
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = stringResource(id = R.string.other_locations), style = WeatherFontTypography.labelMedium, color = Color.White)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Button(
                colors = ButtonDefaults.buttonColors(BlackLight),
                onClick = {
                action()
                navController.navigate(Screens.Places.route)
            }) {
                Text(text = stringResource(id = R.string.manage_locations), style = WeatherFontTypography.labelMedium,
                    modifier = Modifier
                        .background(color = Color.Transparent, shape = RoundedCornerShape(10.dp)))
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Canvas(
            Modifier
                .fillMaxWidth()
                .height(2.dp)) {
            drawLine(
                color = Color.White,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                pathEffect = pathEffect,
                strokeWidth = 2f
            )
        }
    }
}

@Composable
fun TemperatureUnits(action: (TemperatureUnitType) -> Unit) {
    Column(modifier = Modifier
        .padding(start = 30.dp, end = 30.dp, top = 30.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = stringResource(id = R.string.temperature_units), style = WeatherFontTypography.labelMedium, color = Color.White)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                colors = ButtonDefaults.buttonColors(BlackLight),
                onClick = {
                action(TemperatureUnitType.Fahrenheit)
            }) {
                Text(text = stringResource(id = R.string.to_fahrenheit), style = WeatherFontTypography.labelMedium,
                    modifier = Modifier
                        .background(color = Color.Transparent, shape = RoundedCornerShape(10.dp)))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                colors = ButtonDefaults.buttonColors(BlackLight),
                onClick = {
                    action(TemperatureUnitType.Degree)
                }) {
                Text(text = stringResource(id = R.string.to_degree), style = WeatherFontTypography.labelMedium,
                    modifier = Modifier
                        .background(color = Color.Transparent, shape = RoundedCornerShape(10.dp)))
            }
        }
    }
}

@Composable
fun WeatherLoader(message: String? = null) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.bluesky),
            contentDescription = "background_image",
            contentScale = ContentScale.FillBounds
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 10.dp)
                .background(
                    BlackLight
                )
        ) {
            CircularProgressIndicator(color = Color.White)
            message?.run {
                Text(text = this, modifier = Modifier.padding(10.dp), color = Color.White)
            }
        }
    }
}

@Composable
fun ShowWeatherLoader() {
    WeatherLoader(message = stringResource(id = R.string.loader_message))
}

@Composable
fun <LO : LifecycleObserver> LO.ObserveLifecycle(lifecycle: Lifecycle) {
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(this@ObserveLifecycle)
        onDispose {
            lifecycle.removeObserver(this@ObserveLifecycle)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationItemsPReview() {
    WeatherTheme {
        CurrentLocationNavigationItem("Newark", "19") {}
    }
}