package com.example.firstappandmaybethelast.uiactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firstappandmaybethelast.MainActivity
import com.example.firstappandmaybethelast.databinding.LoginpageBinding


const val PREF_NAME = "LoginPage"

class LoginPage: AppCompatActivity() {
    private val binding by lazy {
        LoginpageBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.button2.setOnClickListener {
            checkAuthentication()
        }
        binding.textView11.setOnClickListener{
            // direct to sign up page in website
        }
    }
    private fun gotoMain() = Intent(this, MainActivity::class.java).also {
                                        startActivity(it)
                                        finish()
                                    }
    private fun checkAuthentication() {
        val user = binding.editTextUsername.text.toString()
        val password = binding.editTextPassword.text.toString()
        if(user == "admin" && password == "admin"){
            val preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit()
            preferences.run {
                putString("user",user)
                putString("password",password)
                commit()
            }
            gotoMain()
        }else{
            Toast.makeText(applicationContext,"Not exist Username or incorrect Pasword",Toast.LENGTH_SHORT).show()
            binding.editTextUsername.setText("")
            binding.editTextPassword.setText("")
        }
    }

}