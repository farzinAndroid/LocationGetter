package com.farzin.locationgetter.data.location_service

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationClient @Inject constructor(
    private val client: FusedLocationProviderClient,
    private val locationRequest: LocationRequest
) : LocationInterface {
    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {

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
                client.requestLocationUpdates(
                    locationRequest,
                    locationRequestCallback,
                    Looper.getMainLooper()
                )



            awaitClose { client.removeLocationUpdates(locationRequestCallback) }

        }
    }
}