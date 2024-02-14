package com.example.firstappandmaybethelast

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firstappandmaybethelast.databinding.SplashscreenBinding
import com.example.firstappandmaybethelast.model.ServiceLocator
import com.example.firstappandmaybethelast.musicdata.MusicData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

class FirstScreenActivity : AppCompatActivity() {

    private val binding by lazy {
        SplashscreenBinding.inflate(layoutInflater)
    }
    private var isOnline = false
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
            isOnline = isOnline()
        }
        Toast.makeText(applicationContext,"Checking Internet Connection", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            if(!isOnline){
                Toast.makeText(this@FirstScreenActivity, "No Internet connection", Toast.LENGTH_LONG).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 1000)
            }else{
                CoroutineScope(Dispatchers.IO).launch{
//                val connection = async { MongoClientConnectionExample }.await()
                    withContext(Dispatchers.Main){
                        Toast.makeText(applicationContext,"Success", Toast.LENGTH_SHORT).show()
                    }
                    val result = async { ServiceLocator.apiAction.getSong() }.await()
                    MusicData.musicList = result
                    Log.d(MainActivity.TAG, "onCreate: $result")
                    Intent(this@FirstScreenActivity,MainActivity::class.java).run {
                        startActivity(this)
                    }
                    finish()
                }
            }
        },1000)



    }
    private fun isOnline(): Boolean {
        return try {
            val timeoutMs = 1500
            val sock = Socket()
            val sockAddress: SocketAddress = InetSocketAddress("8.8.8.8", 53)
            sock.connect(sockAddress, timeoutMs)
            sock.close()
            true
        } catch (e: IOException) {
            false
        }
    }
}