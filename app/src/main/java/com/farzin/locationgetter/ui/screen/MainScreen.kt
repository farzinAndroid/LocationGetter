package com.farzin.locationgetter.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farzin.locationgetter.data.model.LocationModel
import com.farzin.locationgetter.viewmodels.LocationViewModel
import com.google.android.gms.location.LocationRequest
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
@Composable
fun MainScreen(
    isLocationPermissionGranted: Boolean,
    isNotificationPermissionGranted: Boolean,
    isLocationSettingsGranted: Boolean,
    locationViewModel: LocationViewModel = hiltViewModel(),
) {

    var locationList by remember { mutableStateOf(emptyList<LocationModel>()) }
    val context = LocalContext.current

    if (isLocationPermissionGranted && isLocationSettingsGranted && isNotificationPermissionGranted) {

        LaunchedEffect(true){
            locationViewModel.locationList.collectLatest {
                val date = Date()
                val formattedDate = SimpleDateFormat("yyyy/mm/dd - hh:mm:ss").format(date)

                val newList = listOf(
                    LocationModel(
                        it.latitude.toString(),
                        it.longitude.toString(),
                        formattedDate
                    )
                )

                locationList += newList

            }
        }

        locationViewModel.showNotification(context)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp)
        ) {
            items(locationList) {
                LocationCard(locationModel = it)
            }
        }

    }else{
        Log.e("TAG","error")
    }
}