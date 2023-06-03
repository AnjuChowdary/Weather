package com.example.weather.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.weather.models.LocationLatAndLon
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale

class LocationUtils {
    companion object {
        fun getCurrentLocality(latLan: LocationLatAndLon, context: Context): String {
            return getLocationLocality(latLan, context = context)
        }

        @SuppressLint("StaticFieldLeak")
        lateinit var locationProvider: FusedLocationProviderClient

        fun getUserLocation(context: Context, result: (LocationLatAndLon) -> Unit) {
            locationProvider = LocationServices.getFusedLocationProviderClient(context)
            var currentUserLocation: LocationLatAndLon

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            locationProvider.lastLocation.addOnSuccessListener {
                Log.d("Last Location Info", "${it.latitude},${it.longitude}")
                currentUserLocation = LocationLatAndLon(it.latitude, it.longitude)
                result(currentUserLocation)
            }
        }

        private fun getLocationLocality(latLan: LocationLatAndLon, context: Context): String {
            var addressText = ""
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(latLan.latitude, latLan.longitude, 1)
                if (addresses?.isNotEmpty() == true) {
                    val address = addresses[0]
                    addressText = address.locality
                    Log.d("geolocation", addressText)
                }
            } catch (e: IOException) {
                Log.d("geolocation", e.message.toString())

            }
            return addressText
        }
    }
}