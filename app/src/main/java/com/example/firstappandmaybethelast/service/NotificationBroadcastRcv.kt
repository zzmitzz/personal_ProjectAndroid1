package com.example.firstappandmaybethelast.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationBroadcastRcv : BroadcastReceiver() {
    val ACTION_NEXT = "NEXT"
    val ACTION_PREV = "PREVIOUS"
    val ACTION_PLAY = "PLAY"
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG,"BroadcastRcv")
        val callServiceIntent = Intent(context,MediaPlayerService::class.java)
        if (intent != null) {
            Log.d(TAG, intent.action ?: "broadcast")
            when(intent.action){
                ACTION_NEXT -> also {
                    callServiceIntent.putExtra("action", ACTION_NEXT).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    context?.startService(callServiceIntent)
                }
                ACTION_PREV -> also {
                    callServiceIntent.putExtra("action", ACTION_PREV).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    context?.startService(callServiceIntent)
                }
                ACTION_PLAY -> also {
                    callServiceIntent.putExtra("action", ACTION_PLAY).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    context?.startService(callServiceIntent)
                }
            }
        }

    }
    companion object {
        const val TAG = "NOTIFICATION_BROADCAST"
    }
}