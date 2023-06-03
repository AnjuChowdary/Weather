package com.example.weather.features

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weather.R
import com.example.weather.enums.Screens
import com.example.weather.ui.theme.BlackLight
import com.example.weather.ui.theme.WeatherFontTypography
import com.example.weather.ui.theme.WeatherTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class LaunchActivity: ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter",
        "UnusedMaterial3ScaffoldPaddingParameter"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.isNavigationBarVisible = false
            val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
            val permissionState = rememberPermissionState(permission)
            WeatherTheme {
                Scaffold {
                    when (permissionState.status) {
                        is PermissionStatus.Granted -> {
                            val navController = rememberNavController()
                            NavigationComponent(mNavController = navController)
                        }
                        is PermissionStatus.Denied -> {
                            permissionState.launchPermissionRequest()
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun WeatherLaunchScreen(navController: NavHostController) {
    Box {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.bluesky),
            contentDescription = "background_image",
            contentScale = ContentScale.FillBounds
        )
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = BlackLight), verticalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(R.drawable.weather),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(top = 5.dp),
                alignment = Alignment.Center,
                colorFilter = ColorFilter.tint(color = Color.White)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = stringResource(id = R.string.app_name), style = WeatherFontTypography.bodyLarge, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color.White)
            LaunchedEffect(Unit) {
                delay(1500)
                navController.popBackStack(Screens.Launch.route, inclusive = true)
                navController.navigate(Screens.Home.route)
            }
        }
    }
}

@Composable
fun NavigationComponent(mNavController: NavHostController) {
    NavHost(navController = mNavController, startDestination = "launch") {
        composable(Screens.Launch.route) {
            WeatherLaunchScreen(mNavController)
        }
        composable(Screens.Home.route) {
            HomeScreen(mNavController)
        }
        composable(Screens.Places.route) {
            AddNewLocation(mNavController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LaunchActivityPreview() {
    WeatherTheme {
        WeatherLaunchScreen(rememberNavController())
    }
}