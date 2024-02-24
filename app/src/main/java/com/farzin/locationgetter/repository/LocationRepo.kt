package com.farzin.locationgetter.repository

import com.farzin.locationgetter.data.location_service.LocationClient
import javax.inject.Inject

class LocationRepo @Inject constructor(
    private val client: LocationClient
) {
    fun getLocationUpdates(interval: Long) = client.getLocationUpdates(interval)
}