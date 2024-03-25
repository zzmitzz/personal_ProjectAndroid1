package com.example.firstappandmaybethelast.uiactivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.firstappandmaybethelast.R
import com.example.firstappandmaybethelast.databinding.ArtistactivityBinding
import com.example.firstappandmaybethelast.ext
import com.example.firstappandmaybethelast.realmdb.Artist
import com.example.firstappandmaybethelast.service.MusicPlayerActivity
import com.example.firstappandmaybethelast.viewmodel.ArtistViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class ArtistActivityLazy: AppCompatActivity() {
    private val binding by lazy {
        ArtistactivityBinding.inflate(layoutInflater)
    }
    private var position = 0
    private lateinit var artist: Artist
    private val viewModel by viewModels<ArtistViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        position = intent.getIntExtra("artist", 0)
        artist = ext.customArtistList[position]
        Picasso.get()
            .load(artist.image)
            .error(R.drawable._31f9ec4b_d0d1_4f4b_a104_94450e11e8da)
            .into(binding.artistBG)
        lifecycleScope.launch {
            viewModel.viewModelShowMusic(artist)
            viewModel.ready.collect {
                Log.d("PlaylistLazy", it.toString() )
                if (it) {
                    binding.loadingOverlayout.visibility = View.INVISIBLE
                    setRecyclerView()
                }
            }
        }
        viewModel.adapter.onItemClick = { it ->
            Intent(applicationContext, MusicPlayerActivity::class.java).apply {
                putExtra("mediaPosition", it)
                startActivity(this)
            }
        }
    }
    private fun setRecyclerView(){
        binding.playlist.run {
            adapter = viewModel.adapter
            setHasFixedSize(true)
        }
    }
}