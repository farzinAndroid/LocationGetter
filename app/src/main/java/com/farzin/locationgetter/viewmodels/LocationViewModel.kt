package com.farzin.locationgetter.viewmodels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.farzin.locationgetter.data.location_service.LocationService
import com.farzin.locationgetter.repository.LocationRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    locationRepo: LocationRepo,
) : ViewModel() {


    val locationList = locationRepo.getLocationUpdates(5000)

    fun showNotification(context: Context){

        Intent(context,LocationService::class.java).apply {
            context.startService(this)
        }

    }
}