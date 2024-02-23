package com.example.firstappandmaybethelast.service

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MediaPlayerService : Service(), MediaPlayer.OnErrorListener{
     //
     private val myBinder = MyBinder()
     var mediaPlayer: MediaPlayer? = null
     private var mediaFile : String = ""
     private var currentSource: String = ""
     private lateinit var songID: String
     private lateinit var musicAction: MusicAction
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
         Log.d("NOTIFICATION_BROADCAST", "setCallback")
     }
     override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
         try {
             Log.d("NOTIFICATION_BROADCAST", "onStartCommand")
             val music = intent?.getParcelableExtra<com.example.firstappandmaybethelast.realmdb.Music>("song")
             if (music != null) {
                 songID = music._id
                 mediaFile = music.musicSource
             }else{
                 Log.d("NOTIFICATION_BROADCAST", "adsf")
                 Log.d("NOTIFICATION_BROADCAST", intent?.getStringExtra("action") ?: "")
                 when(intent?.getStringExtra("action")){
                     ACTION_NEXT -> {
                         musicAction.nextSong()
                     }
                     ACTION_PREV -> {
                        musicAction.prevSong()
                     }
                     ACTION_PLAY -> {
                         musicAction.playPause()
                     }
                 }
             }
         } catch (e: NullPointerException) {
             stopSelf()
         }
         if (mediaFile != "" && currentSource != songID) {

             currentSource = songID
             CoroutineScope(Dispatchers.IO).launch {
                 mediaInitial()
             }
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
         val ACTION_NEXT = "NEXT"
         val ACTION_PREV = "PREVIOUS"
         val ACTION_PLAY = "PLAY"
     }
}