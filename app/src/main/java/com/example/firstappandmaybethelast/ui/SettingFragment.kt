package com.example.firstappandmaybethelast.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.firstappandmaybethelast.databinding.FragmentSettingBinding

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {
    private val binding: FragmentSettingBinding by lazy {
        FragmentSettingBinding.inflate(layoutInflater)
    }
    private var isLogin = false
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
                // TODO
            }
        }else{
            binding.textView7.apply {
                text = "Login"
                setTextColor(Color.GREEN)
                // TODO
            }
        }
    }
    private fun uiSetting() {
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