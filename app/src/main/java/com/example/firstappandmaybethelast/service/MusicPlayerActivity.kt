package com.example.firstappandmaybethelast.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.firstappandmaybethelast.R
import com.example.firstappandmaybethelast.databinding.MusicplayerfragmentBinding
import com.example.firstappandmaybethelast.ext
import com.example.firstappandmaybethelast.realmdb.Music
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import kotlin.random.Random


class MusicPlayerActivity : AppCompatActivity(), MusicAction {
    private val binding by lazy { MusicplayerfragmentBinding.inflate(layoutInflater) }
    private lateinit var buttonPlay: ImageButton
    private lateinit var buttonPrev: ImageButton
    private lateinit var buttonNext: ImageButton
    private lateinit var seekBar: SeekBar
    private lateinit var musicInstance: Music
    private var position = 0
    private var mService: MediaPlayerService? = null
    private var mBound: Boolean = false
    private val handler = Handler(Looper.getMainLooper())
    private var replay = false
    private var shuffle = false
    private var listMusicData = ext.customListMusic
    private var myBitmap: Bitmap? = null
    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, service: IBinder?) {
            val binder = service as MediaPlayerService.MyBinder
            mService = binder.getService
            mService!!.setCallback(this@MusicPlayerActivity)
            mBound = true
        }
        override fun onServiceDisconnected(p0: ComponentName?) {
            mService = null
            mBound = false
        }
    }

    private fun playAudio(){
        Intent(this, MediaPlayerService::class.java).also {
            it.putExtra("song", musicInstance)
            if(!mBound) mBound = bindService(it, connection, BIND_AUTO_CREATE)
            startService(it)
        }
    }
    private fun startRotateCover(){
        Runnable {
            binding.musicCover.animate().rotationBy(360F)
                .withEndAction(Runnable { startRotateCover() }).setDuration(10000).setInterpolator(
                    LinearInterpolator()
                ).start()
        }.run()
    }
    private fun stopRotateCover(){
        binding.musicCover.animate().cancel()
    }
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun seekBarFuture(){
        seekBar.max = mService!!.length
        binding.starttime.text = "00:00"
        binding.endtime.text = SimpleDateFormat("mm:ss").format(mService!!.length)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
               if(!mService!!.mediaPlayerIsNull && fromUser) {
                   mService!!.seekTo(progress)
               }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        updateRuntime()
    }
    private fun updateRuntime(){
        runOnUiThread(object : Runnable {
            @SuppressLint("SimpleDateFormat")
            override fun run() {
                val tmp = mService!!.mediaCurrentPosition
                seekBar.progress = tmp
                binding.starttime.text = SimpleDateFormat("mm:ss").format(tmp)
                mService!!.mediaPlayer?.setOnCompletionListener {
                    if (!replay) {
                        nextSong()
                    } else {
                        lifecycleScope.launch {
                            mService!!.resumePlayer = 0
                            mService!!.resumeMedia()
                            startRotateCover()
                            buttonPlay.setImageResource(R.drawable.stop_fill0_wght400_grad0_opsz24)
                        }
                    }
                }
                handler.postDelayed(this, 500)
            }
        })
    }

     override fun nextSong(){
        if(mService!!._successOnPrepare){
            if(shuffle) {
                position = Random.nextInt(0, listMusicData.size - 1)
            } else {
                position += 1
                position %= listMusicData.size
            }
            initialSongPlayer()
        }
    }
     override fun prevSong(){
         if(mService!!._successOnPrepare){
             position -= 1
             position += listMusicData.size
             position %= (listMusicData.size )
             initialSongPlayer()
         }
    }
    private fun stopMusic(){
        mService!!.pauseMedia()
        stopRotateCover()
        buttonPlay.setImageResource(R.drawable.play_arrow_fill0_wght400_grad0_opsz24)
//        sendNotificationService(R.drawable.play_arrow_fill0_wght400_grad0_opsz24)
    }
    private fun playMusic(){
        mService!!.resumeMedia()
        startRotateCover()
        buttonPlay.setImageResource(R.drawable.stop_fill0_wght400_grad0_opsz24)
//        sendNotificationService(R.drawable.stop_fill0_wght400_grad0_opsz24)
    }
    override fun playPause(){
        if (mService!!.getPlayerStatus) {
            stopMusic()
        } else {
            playMusic()
        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "ChannelId", "Music Player",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
    private fun initialSongPlayer(){
        binding.overLayout.visibility = View.VISIBLE
        musicInstance = listMusicData[position]
        CoroutineScope(Dispatchers.IO).launch {
            val url = URL(musicInstance.imageResource)
            val connection = url.openConnection() as HttpURLConnection
            myBitmap = BitmapFactory.decodeStream(connection.inputStream)
            ext.bitmapCurrentSong = myBitmap
            withContext(Dispatchers.Main) {
                binding.musicCover.setImageBitmap(myBitmap)
            }
        }
        stopRotateCover()
        binding.tvMusicName.text = musicInstance.title
        binding.tvArtistName.text = musicInstance.artist
        binding.playbtn.setImageResource(R.drawable.play_arrow_fill0_wght400_grad0_opsz24)
        lifecycleScope.launch {
            withTimeout(3000){
                try {
                    playAudio()
                    if(mService == null) delay(200L) // For getting mService ready
                    mService!!._successOnPrepare = false
                    while(!mService!!._successOnPrepare){
                        delay(100)
                    }
                    seekBarFuture()
                    buttonListener()
                    loadingSuccess()
                    playMusic()

                }catch (e: Exception){
                    Toast.makeText(
                        this@MusicPlayerActivity,
                        "Time out, Please check your connection",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("Error", e.toString())
                    stopService(Intent(applicationContext, MediaPlayerService::class.java))
                    finish()
                }
            }
        }
    }
    private fun uiBuilding(){
        buttonPlay = binding.playbtn
        buttonPrev= binding.previousBtn
        buttonNext = binding.nextBtn
        seekBar = binding.seekBar
        binding.backMusicBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed()}
        binding.replay.setOnClickListener {
            if(replay){
                replay = false
                binding.replay.setColorFilter(getColor(R.color.black))
            }else{
                replay = true
                binding.replay.setColorFilter(getColor(R.color.purple_200))
            }
        }
        binding.shuffle.setOnClickListener {
            if(shuffle){
                shuffle = false
                binding.shuffle.setColorFilter(getColor(R.color.black))
            }else{
                shuffle = true
                binding.shuffle.setColorFilter(getColor(R.color.purple_200))
            }
        }
    }
    private fun buttonListener(){
        buttonPlay.setOnClickListener {
            playPause()
        }

        binding.nextBtn.setOnClickListener {
            nextSong()
        }
        binding.previousBtn.setOnClickListener {
            prevSong()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        position = intent.getIntExtra(mediaPosition, 0)
        uiBuilding()
        createNotificationChannel()
        binding.constraintCard.visibility = View.GONE
        initialSongPlayer()

    }
    override fun onDestroy() {
        super.onDestroy()
        if(mBound){
            this.unbindService(connection)
            mBound = false
        }
    }
    private fun loadingSuccess() {
        binding.overLayout.visibility = View.GONE
        binding.constraintCard.visibility = View.VISIBLE
    }
    companion object {
        const val musicResource = "songPath"
        const val mediaPosition = "mediaPosition"
        const val TAG = "MusicPlayerActivity"
    }
}