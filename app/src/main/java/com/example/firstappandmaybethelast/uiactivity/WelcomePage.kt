package com.example.firstappandmaybethelast.uiactivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firstappandmaybethelast.MainActivity
import com.example.firstappandmaybethelast.databinding.WelcomepageBinding

class WelcomePage: AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        WelcomepageBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d("Welcome Page", "onCreate")
        binding.button.setOnClickListener{
            val pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
            if(pref.getString("user","") == "" || pref.getString("password","") == "" ){
                Intent(this,LoginPage::class.java).also {
                    startActivity(it)
                }
            }else{
                Intent(this,MainActivity::class.java).also {
                    startActivity(it)
                }
                finish()
            }
        }
        binding.guestmode.setOnClickListener{
            Intent(this,MainActivity::class.java).also {
                startActivity(it)
            }
            Toast.makeText(applicationContext,"You are in Guest Mode", Toast.LENGTH_SHORT).show();
            finish()
        }
    }

}