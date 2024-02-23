package com.farzin.locationgetter.data.location_service

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationClient @Inject constructor(
    private val client: FusedLocationProviderClient,
    private val locationRequest: LocationRequest,
    @ApplicationContext private val context: Context,
) : LocationInterface {
    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isInternetEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && isInternetEnabled) {
                throw LocationInterface.LocationException("gps or internet unavailable")
            }

            val locationRequestCallback = object : LocationCallback() {
                @SuppressLint("SimpleDateFormat")
                override fun onLocationResult(location: LocationResult) {
                    super.onLocationResult(location)
                    location.locations.forEach {

                        launch {
                            send(it)
                        }

                    }
                }
            }

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                client.requestLocationUpdates(
                    locationRequest,
                    locationRequestCallback,
                    Looper.getMainLooper()
                )
            } else {
                throw LocationInterface.LocationException("permission denied")
            }


            awaitClose { client.removeLocationUpdates(locationRequestCallback) }

        }
    }
}