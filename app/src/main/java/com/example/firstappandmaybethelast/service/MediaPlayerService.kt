package com.example.firstappandmaybethelast.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.firstappandmaybethelast.ApplicationClass
import com.example.firstappandmaybethelast.R
import com.example.firstappandmaybethelast.ext
import com.example.firstappandmaybethelast.realmdb.Music
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MediaPlayerService : Service(), MediaPlayer.OnErrorListener{
     //
     private val myBinder = MyBinder()
     var mediaPlayer: MediaPlayer? = null
     private var mediaFile : String = ""
     private lateinit var songID: String
     private lateinit var musicInstance: Music
     private lateinit var musicAction: MusicAction
     private var currentSong: String? = null
     private var mbitmap: Bitmap? = null
     val mediaPlayerIsNull: Boolean
         get() = mediaPlayer == null
     private var _length = 0
     val length: Int
         get() = _length
     val getPlayerStatus: Boolean
         get() = mediaPlayer?.isPlaying ?: false
     val mediaCurrentPosition: Int
         get() = mediaPlayer!!.currentPosition
     var resumePlayer = 0
     var _successOnPrepare: Boolean = false
     private fun stopMedia(){
         if(mediaPlayer!!.isPlaying) mediaPlayer!!.stop()
     }
     fun seekTo(position: Int){
         mediaPlayer!!.seekTo(position)
         resumePlayer = mediaPlayer!!.currentPosition
     }
     fun pauseMedia(){
         if (mediaPlayer!!.isPlaying) {
             mediaPlayer!!.pause()
             resumePlayer = mediaPlayer!!.currentPosition
             startForeground(1, sendNotificationService(R.drawable.play_arrow_fill0_wght400_grad0_opsz24))
         }
     }
     fun resumeMedia(){
         if (!mediaPlayer!!.isPlaying) {
             mediaPlayer!!.seekTo(resumePlayer)
             mediaPlayer!!.start()
             startForeground(1, sendNotificationService(R.drawable.stop_fill0_wght400_grad0_opsz24))
         }
     }
     //
     private fun mediaInitial(){
         resumePlayer = 0
         mediaPlayer?.reset()
         mediaPlayer = MediaPlayer().apply {
             setAudioAttributes(
                 AudioAttributes.Builder()
                     .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                     .setUsage(AudioAttributes.USAGE_MEDIA)
                     .build()
             )
             setDataSource(mediaFile)
             prepareAsync()
             setOnPreparedListener {
                 _length = this.duration
                 _successOnPrepare = true
                 Log.d("TAG", "mediaInitial: $length")
             }
         }
     }


     override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
         //Invoked when there has been an error during an asynchronous operation
         when (what) {
             MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK -> Log.d(
                 "MediaPlayer Error",
                 "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK $extra"
             )

             MediaPlayer.MEDIA_ERROR_SERVER_DIED -> Log.d(
                 "MediaPlayer Error",
                 "MEDIA ERROR SERVER DIED $extra"
             )

             MediaPlayer.MEDIA_ERROR_UNKNOWN -> Log.d(
                 "MediaPlayer Error",
                 "MEDIA ERROR UNKNOWN $extra"
             )
         }
         return false
     }
     override fun onBind(p0: Intent?): IBinder  = myBinder
     fun setCallback(action: MusicAction){
        this.musicAction = action
     }
     override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
         try {
             val music = intent?.getParcelableExtra<com.example.firstappandmaybethelast.realmdb.Music>("song")
             if (music != null) {
                 musicInstance = music
                 songID = music._id
                 mediaFile = music.musicSource
                 if (mediaFile != "" && currentSong != songID) {
                     currentSong = songID
                     CoroutineScope(Dispatchers.IO).launch {
                         mediaInitial()
                         withContext(Dispatchers.IO){
                             startForeground(1,sendNotificationService(R.drawable.stop_fill0_wght400_grad0_opsz24))
                         }
                     }
                 }
             }else{
                 when(intent?.getStringExtra("action")){
                     ACTION_NEXT -> {
                         musicAction.nextSong()
                         startForeground(1,sendNotificationService(R.drawable.play_arrow_fill0_wght400_grad0_opsz24))
                     }
                     ACTION_PREV -> {
                        musicAction.prevSong()
                         startForeground(1,sendNotificationService(R.drawable.play_arrow_fill0_wght400_grad0_opsz24))
                     }
                     ACTION_PLAY -> {
                         musicAction.playPause()
                     }
                     ACTION_KILL -> {
                         onDestroy()
                     }
                 }
             }
         } catch (e: NullPointerException) {
             stopSelf()
         }

         return START_STICKY
     }

     inner class MyBinder : Binder() {
         // return this instance of MediaPlayerService so that clients can call methods on it
         val getService: MediaPlayerService
             get() = this@MediaPlayerService
     }

     override fun onDestroy() {
         super.onDestroy()
         stopMedia()
         stopForeground(STOP_FOREGROUND_REMOVE)
         stopSelf()
     }
    private fun sendNotificationService(playBtn: Int): Notification {
        var bitmap: Bitmap?
        bitmap = ext.bitmapCurrentSong
        if(bitmap == null) BitmapFactory.decodeResource(resources,R.drawable._9c655032_a00e_49ee_be4f_f8a474a29537)
        val prevPending: PendingIntent = PendingIntent
            .getBroadcast(
                applicationContext,
                0,
                Intent(applicationContext, NotificationBroadcastRcv::class.java).setAction(
                    ACTION_PREV
                ),
                PendingIntent.FLAG_IMMUTABLE
            )
        val resumePending: PendingIntent = PendingIntent
            .getBroadcast(
                applicationContext,
                0,
                Intent(applicationContext, NotificationBroadcastRcv::class.java).setAction(
                    ACTION_PLAY
                ),
                PendingIntent.FLAG_IMMUTABLE
            )
        val nextPending: PendingIntent = PendingIntent
            .getBroadcast(
                applicationContext,
                0,
                Intent(applicationContext, NotificationBroadcastRcv::class.java).setAction(
                    ACTION_NEXT
                ),
                PendingIntent.FLAG_IMMUTABLE
            )
        val killPending: PendingIntent = PendingIntent
            .getBroadcast(
                applicationContext,
                0,
                Intent(applicationContext,NotificationBroadcastRcv:: class.java).setAction(
                    ACTION_KILL
                ),
                PendingIntent.FLAG_IMMUTABLE
            )
        return NotificationCompat.Builder(applicationContext, ApplicationClass.CHANNEL_ID_2)
            .setSmallIcon(R.drawable.graphic_eq_fill0_wght400_grad0_opsz24)
            .setContentTitle(musicInstance.title)
            .setLargeIcon(bitmap)
            .setContentText(musicInstance.artist)
            .addAction(R.drawable.skip_previous_fill0_wght400_grad0_opsz24, "Previous", prevPending)
            .addAction(playBtn, "Play", resumePending)
            .addAction(R.drawable.skip_next_fill0_wght400_grad0_opsz24, "Next", nextPending)
            .addAction(R.drawable.more_horiz_fill0_wght400_grad0_opsz24, "kill", killPending)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle())
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSound(null)
            .build()
    }

     companion object{
         val ACTION_NEXT = "NEXT"
         val ACTION_PREV = "PREVIOUS"
         val ACTION_PLAY = "PLAY"
         val ACTION_KILL = "KILL"
     }
}