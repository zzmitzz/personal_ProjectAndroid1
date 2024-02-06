package com.example.firstappandmaybethelast

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firstappandmaybethelast.databinding.SplashscreenBinding
import com.mongodb.client.MongoClients
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
            if (!isOnline()) {
                withContext(Dispatchers.Main){
                    Toast.makeText(this@FirstScreenActivity, "No internet", Toast.LENGTH_LONG).show()
                    delay(5000)
                }
                finish()
            }else{
                connectDB()
            }
        }

//        Handler(Looper.getMainLooper()).postDelayed({
//            Intent(this,MainActivity::class.java).run {
//                startActivity(this)
//            }
//            finish()
//        }, 3000)

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
    private fun connectDB(){
        val hostname = "0op.h.filess.io"
        val database = "ptitweb_answerrock"
        val port = "27018"
        val username = "ptitweb_answerrock"
        val password = "feb556c717d6a25be693e5fb2da7bdb2edc89c92"
        val connectionString = String.format(
            "mongodb://%s:%s@%s:%s/%s", username, password, hostname, port, database
        )
        Log.d("FirstScreen", connectionString)
        val mongoClient = MongoClients.create(connectionString)
        val collections = mongoClient.getDatabase(database).listCollectionNames().toSet()
        Log.d("FirstScreen",collections.toString())
//        mongoClient.use {
//            val databases: List<Document> = it.listDatabases().into(
//                ArrayList<Document>()
//            )
//            Log.d("FirstScreen", databases.toString())
//        }
    }
}