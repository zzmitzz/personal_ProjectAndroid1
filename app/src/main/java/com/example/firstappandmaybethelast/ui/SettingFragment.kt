package com.example.firstappandmaybethelast.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.firstappandmaybethelast.R
import com.example.firstappandmaybethelast.databinding.FragmentSettingBinding
import com.example.firstappandmaybethelast.ext
import com.example.firstappandmaybethelast.uiactivity.LoginPage
import com.example.firstappandmaybethelast.uiactivity.PREF_NAME
import com.example.firstappandmaybethelast.uiactivity.WelcomePage
import com.squareup.picasso.Picasso

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {
    private val binding: FragmentSettingBinding by lazy {
        FragmentSettingBinding.inflate(layoutInflater)
    }
    private var isLogin = ext.isLogin
    private var isDarkMode = false
    private var language = false
    private var avatarImage: ImageView? = null
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        uiSetting()
        return binding.root
    }
    private fun loginHandle(){
        if(isLogin){
            binding.textView7.apply {
                text = "Logout"
                setTextColor(Color.RED)
            }
            binding.textView7.setOnClickListener{
                Intent(context, LoginPage::class.java).also {
                    startActivity(it)
                }
                val preferences = context?.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
                val editor = preferences?.edit()
                editor?.clear()
                editor?.apply()
                activity?.finish()
            }
        }else{
            binding.textView7.apply {
                text = "Login"
                setTextColor(Color.GREEN)
            }
            binding.textView7.setOnClickListener{
                Intent(context, WelcomePage::class.java).also {
                    startActivity(it)
                }
                val preferences = context?.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
                val editor = preferences?.edit()
                editor?.clear()
                editor?.apply()
                activity?.finish()
            }
        }
    }
    private fun uiSetting() {
        Picasso
            .get()
            .load(ext.user?.profilePictureUrl)
            .placeholder(R.drawable.baseline_account_circle_24)
            .into(binding.imageView3)
        binding.username.text = "Hi, ${ext.user?.username ?: "Unknown"}"
        binding.apply {
            if(switch1.isChecked){
                Toast.makeText(context, "Dark Mode", Toast.LENGTH_SHORT).show()
            }
            if(switch2.isChecked){
                Toast.makeText(context, "English", Toast.LENGTH_SHORT).show()
            }
        }
        loginHandle()
    }

}