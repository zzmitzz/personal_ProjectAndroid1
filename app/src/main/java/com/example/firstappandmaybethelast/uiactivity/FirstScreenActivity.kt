package com.example.firstappandmaybethelast.uiactivity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firstappandmaybethelast.databinding.SplashscreenBinding
import com.example.firstappandmaybethelast.ext
import com.example.firstappandmaybethelast.model.ServiceLocator
import com.example.firstappandmaybethelast.musicdata.Album
import com.example.firstappandmaybethelast.musicdata.Artist
import com.example.firstappandmaybethelast.musicdata.Music
import io.realm.kotlin.UpdatePolicy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        Handler(Looper.getMainLooper()).postDelayed({
            if(!isOnline){
                Toast.makeText(this@FirstScreenActivity, "No Internet connection", Toast.LENGTH_LONG).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 1000)
            }else{
                CoroutineScope(Dispatchers.IO).launch{
                    Log.d("firstScreen", "Data preparing")
                    runOnUiThread {
                        binding.processText.text = "Downloading Music..."
                    }
                    var result: List<Music>? = ServiceLocator.apiAction.getSong()
                    runOnUiThread{
                        binding.processText.text = "Downloading Album..."
                    }
                    val resultAlbum: List<Album> = ServiceLocator.apiAction.getAlbum()
                    runOnUiThread{
                        binding.processText.text = "Downloading Artist..."
                    }
                    val resultArtist: List<Artist> = ServiceLocator.apiAction.getArtist()
                    ext.realm.write {
                        deleteAll()
                        for(music in result!!){
                            copyToRealm(ext.cvtToRealmMusic(music), updatePolicy = UpdatePolicy.ALL)
                        }
                        for(album in resultAlbum){
                            copyToRealm(ext.cvtToRealmAlbum(album), updatePolicy = UpdatePolicy.ALL)
                        }
                        for(artist in resultArtist){
                            copyToRealm(ext.cvtToRealmArtist(artist), updatePolicy = UpdatePolicy.ALL)
                        }
                    }
                    Intent(this@FirstScreenActivity, WelcomePage::class.java).run {
                        startActivity(this)
                        result = null
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