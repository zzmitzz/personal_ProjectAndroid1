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
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.firstappandmaybethelast.R
import com.example.firstappandmaybethelast.musicdata.Music
import com.example.firstappandmaybethelast.service.MusicPlayerActivity.Companion.CHANNEL_ID


class MediaPlayerService : Service(), MediaPlayer.OnErrorListener{
     //
     private val myBinder = MyBinder()
     var mediaPlayer: MediaPlayer? = null
     private lateinit var mediaFile : String
     private var currentSource: String = ""
     private lateinit var songID: String
    private lateinit var songTitle: String
    private lateinit var songArtist: String
    private lateinit var songCover: Bitmap
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
     private var _successOnPrepare: Boolean = false
     val successOnPrepare get() = _successOnPrepare

     fun playMedia() = mediaPlayer!!.start()
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
         }


     }
     fun resumeMedia(){
         if (!mediaPlayer!!.isPlaying) {
             mediaPlayer!!.seekTo(resumePlayer)
             mediaPlayer!!.start()
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
             setOnPreparedListener{
                 _length = mediaPlayer!!.duration
                 _successOnPrepare = true
             }
             setOnErrorListener{ _, _, _ ->
                 Toast.makeText(applicationContext,"Check your connection",Toast.LENGTH_SHORT).show()
                 false
             }


         }
     }
     private fun sendNotificationService(){
         val notificationIntent = Intent(this, MusicPlayerActivity::class.java)
         val pendingIntent = PendingIntent.getActivity(
             this, 0,
             notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT )
         val musicCover = BitmapFactory.decodeResource(resources,R.drawable.db)
         val remoteView = RemoteViews(packageName, R.layout.playernotification)
         remoteView.setTextViewText(R.id.title_notification,songTitle)
         remoteView.setTextViewText(R.id.textView8,songArtist)
         remoteView.setImageViewBitmap(R.id.imageView,BitmapFactory.decodeResource(resources,R.drawable.db))
         val prevPending: PendingIntent = PendingIntent
             .getBroadcast(this,0,Intent(this,NotificationBroadcastRcv::class.java).setAction(
                 ACTION_PREV),PendingIntent.FLAG_IMMUTABLE)
         val resumePending: PendingIntent = PendingIntent
             .getBroadcast(this,0,Intent(this,NotificationBroadcastRcv::class.java).setAction(
                 ACTION_PLAY),PendingIntent.FLAG_IMMUTABLE)
         val nextPending: PendingIntent = PendingIntent
             .getBroadcast(this,0,Intent(this,NotificationBroadcastRcv::class.java).setAction(
                 ACTION_NEXT),PendingIntent.FLAG_IMMUTABLE)
         remoteView.setOnClickPendingIntent(R.id.preBtn,prevPending)
         remoteView.setOnClickPendingIntent(R.id.resumebtn,resumePending)
         remoteView.setOnClickPendingIntent(R.id.nexBtn,nextPending)

         val notification: Notification = NotificationCompat.Builder(this,CHANNEL_ID)
             .setSmallIcon(R.drawable.graphic_eq_fill0_wght400_grad0_opsz24)
             .setContentTitle("P_Music")
             .setContentText("Play_music")
             .setCustomContentView(remoteView)
             .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
             .setSound(null)
             .build()
         startForeground(1337, notification)
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

     override fun onRebind(intent: Intent?) {
         super.onRebind(intent)
     }
     override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

             try {
                 val bundle = intent?.extras
                 val music = bundle?.get("song") as Music
                 songID = music.id
                 songTitle = music.title
                 songArtist = music.artist
                 songCover = BitmapFactory.decodeResource(resources, music.imageResource)
                 mediaFile = music.musicSource
             } catch (e: NullPointerException) {
                 stopSelf()
             }
             if (mediaFile != "" && songID != currentSource) {
                 mediaInitial()
                 sendNotificationService()
                 currentSource = songID
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
         mediaPlayer?.release()
         stopSelf()
     }
     companion object{
         const val TAG = "MediaPlayerService"
         val ACTION_NEXT = "NEXT"
         val ACTION_PREV = "PREVIOUS"
         val ACTION_PLAY = "PLAY"
     }
}