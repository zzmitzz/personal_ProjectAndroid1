package com.example.firstappandmaybethelast

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firstappandmaybethelast.databinding.SplashscreenBinding
import com.example.firstappandmaybethelast.model.ServiceLocator
import com.example.firstappandmaybethelast.musicdata.Music
import io.realm.kotlin.UpdatePolicy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
                    var result: List<Music>? = async { ServiceLocator.apiAction.getSong() }.await()
                    // Write to local db Realm
                    async {
                        ext.realm.write {
                            if(ext.getMusicData().size != result!!.size){
                                deleteAll()
                                for((index,music) in result!!.withIndex()){
                                    copyToRealm(cvtToRealm(music,index), updatePolicy = UpdatePolicy.ALL)
                                }
                            }
                        }
                        Intent(this@FirstScreenActivity,MainActivity::class.java).run {
                            startActivity(this)
                            result = null
                        }
                        finish()
                    }.await()
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
    private fun cvtToRealm(music: Music, index: Int): com.example.firstappandmaybethelast.realmdb.Music{

        return com.example.firstappandmaybethelast.realmdb.Music().apply {
            _id = music.id
            title = music.title
            artist = music.artist
            genre = music.genre
            musicSource = music.musicSource
            imageResource = music.imageResource
            isFavorite = false
            order = index
        }
    }
}