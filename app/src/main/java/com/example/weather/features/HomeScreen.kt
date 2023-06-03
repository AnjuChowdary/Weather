package com.example.weather.features

import android.annotation.SuppressLint
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weather.R
import com.example.weather.commoncomposables.AddOtherLocations
import com.example.weather.commoncomposables.CurrentLocationNavigationItem
import com.example.weather.commoncomposables.ObserveLifecycle
import com.example.weather.commoncomposables.SettingsNavigationItem
import com.example.weather.commoncomposables.ShowWeatherLoader
import com.example.weather.commoncomposables.TemperatureUnits
import com.example.weather.data.datautil.NetworkResponse
import com.example.weather.enums.InformationType
import com.example.weather.enums.NavigationItemType
import com.example.weather.extensions.capitalize
import com.example.weather.extensions.firstWord
import com.example.weather.extensions.toDate
import com.example.weather.ui.theme.BlackLight
import com.example.weather.ui.theme.SkyBlueDark
import com.example.weather.ui.theme.WeatherFontTypography
import com.example.weather.utils.CommonUtils
import com.example.weather.utils.CommonUtils.Companion.navigationItems
import com.example.weather.viewmodels.WeatherViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter",
    "UnusedMaterial3ScaffoldPaddingParameter"
)
@Composable
fun HomeScreen(navController: NavController) {
    val mTAG = "HomeScreen"
    val context = LocalContext.current

    val mViewModel: WeatherViewModel by viewModel()
    mViewModel.ObserveLifecycle(lifecycle = LocalLifecycleOwner.current.lifecycle)

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    OnLifecycleEvent { _, event ->
        when(event) {
            Lifecycle.Event.ON_RESUME -> {
                if(CommonUtils.fetchCity(context = context).isNotEmpty()) {
                    mViewModel.fetchWeatherForCity(context = context, city = CommonUtils.fetchCity(context = context))
                }
            }
            else -> {}
        }
    }

    LaunchedEffect(key1 = mViewModel.mWeatherResponse.value) {
        mViewModel.mWeatherResponse.value.run {
            when (this@run) {
                is NetworkResponse.Loading -> {
                    Log.i(mTAG, "HomeScreen: Loading")
                    mViewModel.showLoader.value = true
                }
                is NetworkResponse.Success -> {
                    mViewModel.showLoader.value = false
                    Log.i(mTAG, "HomeScreen: Success")
                    Log.i(mTAG, mViewModel.mWeatherResponse.value.toString())
                }
                is NetworkResponse.Error -> {
                    Log.i(mTAG, "HomeScreen: Error")
                    mViewModel.showLoader.value = false
                    Toast.makeText(context, "Unable to fetch data for this city", Toast.LENGTH_LONG).show()
                }
                else -> {
                    //No implementation
                }
            }
        }
    }

    LaunchedEffect(key1 = mViewModel.mLocality.value) {
        mViewModel.mLocality.value.run {
            takeIf { this.isNotEmpty() }?.run {
                mViewModel.fetchWeatherForCity(context = context, city = this)
            }
        }
    }

    if(mViewModel.showLoader.value) {
        ShowWeatherLoader()
    } else {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(drawerContainerColor = SkyBlueDark) {
                    Spacer(Modifier.height(12.dp))
                    navigationItems.forEach { item ->
                        when(item.itemType) {
                            NavigationItemType.Settings -> {
                                SettingsNavigationItem {
                                    scope.launch { drawerState.close() }
                                    context.startActivity(Intent(Settings.ACTION_SETTINGS))
                                }
                            }
                            NavigationItemType.Home -> {
                                CurrentLocationNavigationItem(locality = mViewModel.mLocality.value.firstWord(),
                                    temp = mViewModel.mTempData.value.temperature) {
                                    scope.launch { drawerState.close() }
                                }
                            }
                            NavigationItemType.AddLocation -> {
                                AddOtherLocations(navController = navController) {
                                    scope.launch { drawerState.close() }
                                }
                            }
                            NavigationItemType.TempUnit -> {
                                TemperatureUnits {
                                    scope.launch { drawerState.close() }
                                    mViewModel.convertTempUnit(context = context, unitType = it)
                                }
                            }
                        }
                    }
                }
            }, content = {
                Box {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(R.drawable.bluesky),
                        contentDescription = "background_image",
                        contentScale = ContentScale.FillBounds
                    )
                    Scaffold(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                        containerColor = Color.Transparent,
                        contentColor = Color.Transparent,
                        topBar = {
                            TopAppBar(
                                title = {},
                                colors = TopAppBarDefaults.smallTopAppBarColors(Color.Transparent),
                                navigationIcon = {
                                    IconButton(
                                        onClick = {
                                            scope.launch { drawerState.open() }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Menu,
                                            contentDescription = "Drawer Icon",
                                            tint = Color.White
                                        )
                                    }
                                },
                                actions = {
                                    IconButton(onClick = {
                                        mViewModel.getLocality(context = context)
                                    }) {
                                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = "current location", tint = Color.White)
                                    }
                                }
                            )
                        },
                        content = {
                            Column(modifier = Modifier
                                .fillMaxSize()
                                .padding(it)
                            ) {
                                WeatherInformation(mViewModel = mViewModel)
                            }
                        })
                }
            })
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WeatherInformation(mViewModel: WeatherViewModel) {
    Column(modifier = Modifier
        .padding(top = 20.dp, bottom = 50.dp, start = 30.dp, end = 30.dp)) {
        Column {
            Card(modifier = Modifier
                .fillMaxWidth(),
                colors = CardDefaults.cardColors(BlackLight)
                , shape = RoundedCornerShape(10.dp)) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(text = mViewModel.mLocality.value, color = Color.White, style = WeatherFontTypography.labelMedium)
                    Text(text = mViewModel.mTempData.value.temperature, color = Color.White, style = WeatherFontTypography.titleLarge)
                    Text(text = mViewModel.mWeatherResponse.value.response?.weather?.get(0)?.description?.capitalize() ?: "Sunny", color = Color.White, style = WeatherFontTypography.labelMedium)
                    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp)) {
                        Text(text = "Feels Like " + mViewModel.mTempData.value.feelsLike, color = Color.White, style = WeatherFontTypography.labelMedium)
                        GlideImage(model = CommonUtils.weatherIconUrl(mViewModel.mWeatherResponse.value.response?.weather?.get(0)?.icon ?: "10d"),
                            contentDescription = null,
                            modifier = Modifier
                                .width(80.dp)
                                .height(80.dp),
                            contentScale = ContentScale.FillBounds)

                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(20.dp))
            InfoCard(information = mViewModel.mWeatherResponse.value.response?.main?.humidity.toString() + "%",
                informationType = InformationType.Humidity,
                res = R.drawable.humidity)
            Spacer(modifier = Modifier.height(20.dp))
            InfoCard(information = mViewModel.mTempData.value.minimumTemperature,
                informationType = InformationType.MinTemp,
                res = R.drawable.mintemp)
            Spacer(modifier = Modifier.height(20.dp))
            InfoCard(information = mViewModel.mTempData.value.maximumTemperature,
                informationType = InformationType.MaxTemp,
                res = R.drawable.maxtemp)
            Spacer(modifier = Modifier.height(20.dp))
            InfoCard(information = mViewModel.mWeatherResponse.value.response?.wind?.speed.toString(),
                informationType = InformationType.WindSpeed,
                res = R.drawable.wind)
            Spacer(modifier = Modifier.height(20.dp))
            InfoCard(information = mViewModel.mWeatherResponse.value.response?.sys?.sunrise?.toDate().toString(),
                informationType = InformationType.Sunrise,
                res = R.drawable.sun)
            Spacer(modifier = Modifier.height(20.dp))
            InfoCard(information = mViewModel.mWeatherResponse.value.response?.sys?.sunset?.toDate().toString(),
                informationType = InformationType.Sunset,
                res = R.drawable.sunset)
        }
    }
}

@Composable
fun InfoCard(information: String, informationType: InformationType, res: Int) {
    Card(modifier = Modifier
        .fillMaxWidth(),
        colors = CardDefaults.cardColors(BlackLight)
        , shape = RoundedCornerShape(10.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(BlackLight)
            .padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(text = informationType.toString(), style = WeatherFontTypography.labelMedium, color = Color.White)
                Text(text = information, style = WeatherFontTypography.labelMedium, color = Color.White)
            }
            Image(
                painter = painterResource(res),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(top = 5.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}