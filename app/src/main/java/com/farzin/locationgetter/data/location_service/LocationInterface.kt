package com.farzin.locationgetter.data.location_service

import android.location.Location
import kotlinx.coroutines.flow.Flow


interface LocationInterface {

    fun getLocationUpdates(interval:Long):  Flow<Location>

    class LocationException(message:String):Exception()

}