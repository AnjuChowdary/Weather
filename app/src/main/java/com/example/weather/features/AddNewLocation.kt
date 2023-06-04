package com.example.weather.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.ContentAlpha
import com.example.weather.R
import com.example.weather.enums.SearchWidgetState
import com.example.weather.ui.theme.BlackLight
import com.example.weather.ui.theme.WeatherFontTypography
import com.example.weather.ui.theme.WeatherTheme
import com.example.weather.utils.CommonUtils
import com.example.weather.enums.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewLocation(navController: NavHostController) {
    var searchWidgetState by remember{ mutableStateOf(SearchWidgetState.Closed) }
    var searchTextState by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current
    Box {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.bluesky),
            contentDescription = "Background Image",
            contentScale = ContentScale.FillBounds
        )
        Scaffold(modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            topBar = {
                if (searchWidgetState == SearchWidgetState.Open) {
                    SearchBar(searchWidgetState = {
                        searchWidgetState = it
                    }, state = {
                        searchTextState = it
                    })
                } else {
                    CityTopBar(searchWidgetState = { state ->
                        searchWidgetState = state
                    }, onBackClick = {
                        navController.popBackStack()
                    })
                }
            }, content = { it ->
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(it)) {
                    Cities(searchTextState) {
                        CommonUtils.saveCity(context = context, it)
                        navController.popBackStack(Screens.Places.route, inclusive = true)
                        navController.navigate(Screens.Home.route)
                    }
                }
            })
    }
}

@Composable
fun Cities(state: TextFieldValue, action: (String) -> Unit) {
    val context = LocalContext.current
    val cities = CommonUtils.loadCities(context)
    val filteredCities = if(state.text.isEmpty()) {
        cities
    } else {
        val resultList = ArrayList<String>()
        for (city in cities) {
            if (city.lowercase()
                    .contains(state.text.lowercase())
            ) {
                resultList.add(city)
            }
        }
        resultList
    }
    Column(modifier = Modifier
        .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
        .fillMaxSize()) {
        LazyColumn(modifier = Modifier
            .fillMaxHeight()
            .padding(top = 10.dp), state = rememberLazyListState(), verticalArrangement = Arrangement.spacedBy(8.dp), content = {
            items(filteredCities) { city ->
                Card(modifier = Modifier
                    .fillMaxWidth(), shape = RoundedCornerShape(10.dp), colors = CardDefaults.cardColors(
                    BlackLight)
                ){
                    Text(text = city, style = WeatherFontTypography.labelMedium, modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            action(city)
                        }, color = Color.White)
                }
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchWidgetState: (SearchWidgetState) -> Unit, state: (TextFieldValue) -> Unit) {
    var searchTextState by remember { mutableStateOf(TextFieldValue("")) }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .height(56.dp),
        color = Color.Transparent
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = searchTextState,
            onValueChange = {
                state(it)
                searchTextState = it
            },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    text = "Search",
                    style = WeatherFontTypography.labelMedium,
                    color = Color.White
                )
            },
            singleLine = true,
            maxLines = 1,
            leadingIcon = {
                Icon(
                    modifier = Modifier.alpha(ContentAlpha.high),
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search Icon",
                    tint = Color.White
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (searchTextState.text.isNotEmpty() || searchTextState.text.isNotBlank()) {
                            searchTextState = TextFieldValue("")
                        } else {
                            searchWidgetState(SearchWidgetState.Closed)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close Icon",
                        tint = Color.White
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions (
                onSearch = {
                    searchWidgetState(SearchWidgetState.Closed)
                }
            ),
            textStyle = WeatherFontTypography.labelMedium
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityTopBar(searchWidgetState: (SearchWidgetState) -> Unit, onBackClick: () -> Unit) {
    TopAppBar( title = {
        Text(text = "Search Location", style = WeatherFontTypography.labelMedium, color = Color.White)
    },
        navigationIcon = {
            IconButton(
                onClick = {
                    onBackClick()
                }
            ) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Drawer Icon", tint = Color.White)
            }
        },
        actions = {
            IconButton(onClick = { searchWidgetState(SearchWidgetState.Open) }) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "",
                    tint = Color.White,
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(Color.Transparent)
    )
}

@Preview(showBackground = true)
@Composable
fun AddNewLocationPreview() {
    WeatherTheme {
        AddNewLocation(rememberNavController())
    }
}
