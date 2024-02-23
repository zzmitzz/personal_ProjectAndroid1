package com.example.firstappandmaybethelast.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.firstappandmaybethelast.ApplicationClass.Companion.ACTION_NEXT
import com.example.firstappandmaybethelast.ApplicationClass.Companion.ACTION_PLAY
import com.example.firstappandmaybethelast.ApplicationClass.Companion.ACTION_PREV

class NotificationBroadcastRcv : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG,"BroadcastRcv")
        val callIntent = Intent(context, MediaPlayerService::class.java)
        if (intent != null) {
            Log.d(TAG,"${intent.action}")
            when(intent.action){
                ACTION_NEXT -> also {
                    callIntent.putExtra("action", ACTION_NEXT)
                    context?.startService(callIntent)
                }
                ACTION_PREV -> also {
                    callIntent.putExtra("action", ACTION_PREV)
                    context?.startService(callIntent)
                }
                ACTION_PLAY -> also {
                    callIntent.putExtra("action", ACTION_PLAY)
                    context?.startService(callIntent)
                }
            }
        }
    }
    companion object {
        const val TAG = "NOTIFICATION_BROADCAST"
    }
}