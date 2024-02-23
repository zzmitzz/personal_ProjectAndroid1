package com.example.firstappandmaybethelast

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class ApplicationClass: Application() {
    companion object {
        const val  CHANNEL_ID_1 = "CHANNEL_1"
        const val CHANNEL_ID_2 = "CHANNEL_2"
        const val ACTION_NEXT = "NEXT"
        const val ACTION_PREV = "PREVIOUS"
        const val ACTION_PLAY = "PLAY"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel : NotificationChannel = NotificationChannel(
                CHANNEL_ID_1, "Channel 1", NotificationManager.IMPORTANCE_HIGH)
            channel.description = "This is channel 1"
            val channel2: NotificationChannel = NotificationChannel(
                CHANNEL_ID_2, "Channel 2", NotificationManager.IMPORTANCE_LOW)
            channel2.description = "This is channel 2"
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
            notificationManager.createNotificationChannel(channel2)
        }
    }
}