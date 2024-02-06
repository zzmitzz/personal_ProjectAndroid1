package com.example.firstappandmaybethelast.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.firstappandmaybethelast.R
import com.example.firstappandmaybethelast.databinding.MusicplayerfragmentBinding
import com.example.firstappandmaybethelast.musicdata.Music
import com.example.firstappandmaybethelast.musicdata.MusicData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class MusicPlayerActivity : AppCompatActivity() {
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
    private var listMusicData = MusicData.musicList
    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, service: IBinder?) {
            val binder = service as MediaPlayerService.MyBinder
            mService = binder.getService
        }
        override fun onServiceDisconnected(p0: ComponentName?) {
            mService = null
        }
    }
    private fun playAudio(music: Music){
        Intent(this, MediaPlayerService::class.java).also {
            val bundle = Bundle()
            bundle.putSerializable("song",music)
            it.putExtras(bundle)
            mBound = bindService(it, connection, BIND_AUTO_CREATE)
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
                        CoroutineScope(Job() + Dispatchers.Main).launch {
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
    private fun nextSong(){
        if(replay) listMusicData.shuffled()
        else listMusicData = MusicData.musicList
        position += 1
        position %= listMusicData.size
        initialSongPlayer()
    }
    private fun prevSong(){
        position -= 1
        position %= listMusicData.size
        initialSongPlayer()
    }
    private fun initialSongPlayer(){
        musicInstance = listMusicData[position]
        binding.musicCover.setImageResource(musicInstance.imageResource)
        binding.tvMusicName.text = musicInstance.title
        binding.tvArtistName.text = musicInstance.artist
        stopRotateCover()
        binding.playbtn.setImageResource(R.drawable.play_arrow_fill0_wght400_grad0_opsz24)
        CoroutineScope(Job() + Dispatchers.Main).launch {
                playAudio(musicInstance)
                delay(200L)
                while(!mService!!.successOnPrepare){
                    delay(50)
                }
                if(mService!!.getPlayerStatus){
                    startRotateCover()
                    buttonPlay.setImageResource(R.drawable.stop_fill0_wght400_grad0_opsz24)
                }
                seekBarFuture()
                buttonListener()

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
            if (mService!!.getPlayerStatus) {
                mService!!.pauseMedia()
                stopRotateCover()
                buttonPlay.setImageResource(R.drawable.play_arrow_fill0_wght400_grad0_opsz24)
            } else {
                mService!!.resumeMedia()
                startRotateCover()
                buttonPlay.setImageResource(R.drawable.stop_fill0_wght400_grad0_opsz24)
            }
        }

        binding.nextBtn.setOnClickListener {
            nextSong()
        }
        binding.previousBtn.setOnClickListener {
            prevSong()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,"P_Music",NotificationManager.IMPORTANCE_DEFAULT)
            channel.setSound(null,null)
            val notification = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notification.createNotificationChannel(channel)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        position = intent.getIntExtra(mediaPosition, 0)
        createNotificationChannel()
        uiBuilding()

    }
    override fun onResume() {
        initialSongPlayer()

        super.onResume()
    }
    override fun onDestroy() {
        super.onDestroy()
        if(mBound){
            this.unbindService(connection)
            mBound = false
        }
    }
    companion object {
        const val musicResource = "songPath"
        const val mediaPosition = "mediaPosition"
        const val TAG = "MusicPlayerActivity"
        const val CHANNEL_ID: String = "Now playing"
    }
}