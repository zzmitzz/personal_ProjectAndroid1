package com.example.firstappandmaybethelast.uiactivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.firstappandmaybethelast.MainActivity
import com.example.firstappandmaybethelast.databinding.LoginpageBinding
import com.example.firstappandmaybethelast.model.ServiceLocator
import com.example.firstappandmaybethelast.musicdata.User
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


const val PREF_NAME = "LoginPage"

class LoginPage: AppCompatActivity() {
    private val binding by lazy {
        LoginpageBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.progressBar.visibility = View.INVISIBLE
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
        val body: Map<String,Any> = mapOf(
            "user" to user,
            "password" to password
        )
        var userDB = "admin"
        var passwordDB = "admin"
        val gson = Gson()
        var invalid = true
        lifecycleScope.launch{
            ServiceLocator.apiAction.authUser(body).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    var responseBody = response.body()?.string()
                    if(responseBody != null){
                        responseBody = responseBody.substring(10,responseBody.length-1)
                        if(responseBody.length < 100){
                            invalid = true
                        }else{
                            var user: User = gson.fromJson(responseBody, User::class.java)
                            invalid = false
                            Log.d("AuthUser", user.username)
                        }
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                }
            })
            runOnUiThread {
                binding.progressBar.visibility = View.VISIBLE
            }
            delay(1000)
            if(!invalid){
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
            runOnUiThread {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

}