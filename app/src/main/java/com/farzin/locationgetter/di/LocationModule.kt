package com.farzin.locationgetter.di

import android.app.NotificationManager
import android.content.Context
import android.location.LocationManager
import com.farzin.locationgetter.data.location_service.LocationClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {


    @Provides
    fun provideFusedLocationClient(@ApplicationContext context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)


    @Provides
    fun provideLocationRequest() : LocationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        5000,
    ).build()

    @Provides
    fun provideLocationClient(
        fusedLocationProviderClient: FusedLocationProviderClient,
        locationRequest: LocationRequest,
        @ApplicationContext context: Context,
        locationManager: LocationManager
    ) : LocationClient = LocationClient(
        fusedLocationProviderClient,
        locationRequest
    )

    @Provides
    fun provideCoroutines() = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ) : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    fun provideLocationManager(
        @ApplicationContext context: Context
    ) : LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

}