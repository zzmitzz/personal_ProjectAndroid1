package com.example.firstappandmaybethelast

import androidx.appcompat.app.AppCompatActivity
import com.example.firstappandmaybethelast.databinding.ActivityMainBinding
import com.example.firstappandmaybethelast.ui.FavoritePage
import com.example.firstappandmaybethelast.ui.HomeFragment
import com.example.firstappandmaybethelast.ui.NearbyFragent
import com.example.firstappandmaybethelast.ui.SearchFragment
import com.example.firstappandmaybethelast.ui.SettingFragment

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val bottomBar = binding.bottomNav
        bottomBar.setOnItemSelectedListener { it ->
            changeFragment(it.itemId)
            return@setOnItemSelectedListener true
        }
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainerView, HomeFragment()).commit()

    }
    private fun changeFragment(item: Int){
        when(item) {
            R.id.home_app -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, HomeFragment()).commit()
//                    true
            }
            R.id.searching_app -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, SearchFragment()).commit()
//                    true
            }
            R.id.nearby_app -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, NearbyFragent()).commit()
//                    true
            }
            R.id.setting_app -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, SettingFragment()).commit()
//                    true
            }
            R.id.favourite -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, FavoritePage()).commit()
            }
        }

    }

    companion object {

    }
}