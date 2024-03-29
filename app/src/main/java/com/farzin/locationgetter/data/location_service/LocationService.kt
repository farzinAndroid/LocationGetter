package com.farzin.locationgetter.data.location_service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.farzin.locationgetter.R
import com.farzin.locationgetter.util.Constants.NOTIF_CHANNEL_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    lateinit var coroutineScope : CoroutineScope

    @Inject
    lateinit var locationClient: LocationClient

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        start()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {



        val notification = NotificationCompat.Builder(this,NOTIF_CHANNEL_ID)
            .setContentTitle("Tracking Location")
            .setContentText("Location : null")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)


        locationClient.getLocationUpdates(5000)
            .catch {e->
                e.printStackTrace()
            }
            .onEach {
                val updatedNotification = notification
                    .setContentText("Location -> (lat : ${it.latitude}) (${it.longitude})")

                notificationManager.notify(1,updatedNotification.build())
            }
            .launchIn(coroutineScope)

        startForeground(1,notification.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}