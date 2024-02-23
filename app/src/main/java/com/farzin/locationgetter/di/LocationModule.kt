package com.farzin.locationgetter.di

import android.app.NotificationManager
import android.content.Context
import com.farzin.locationgetter.data.location_service.LocationClient
import com.farzin.locationgetter.data.location_service.LocationInterface
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
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

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
        @ApplicationContext context: Context
    ) : LocationClient = LocationClient(
        fusedLocationProviderClient,
        locationRequest,
        context
    )

    @Provides
    fun provideCoroutines() = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ) = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

}