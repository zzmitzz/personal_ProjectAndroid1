package com.example.firstappandmaybethelast.uiactivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.firstappandmaybethelast.R
import com.example.firstappandmaybethelast.databinding.FragmentPlaylistBinding
import com.example.firstappandmaybethelast.ext
import com.example.firstappandmaybethelast.realmdb.Album
import com.example.firstappandmaybethelast.service.MusicPlayerActivity
import com.example.firstappandmaybethelast.viewmodel.PlaylistActivityViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

// Not set the view pager yet, Update it soon. Now let fuck up with activity


class PlaylistActivityLazy: AppCompatActivity() {
    private val binding by lazy {
        FragmentPlaylistBinding.inflate(layoutInflater)
    }
    private var position = 0
    private val viewModel by viewModels<PlaylistActivityViewModel>()
    private lateinit var album : Album
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        position = intent.getIntExtra("album", 0)
        album = ext.customAlbumList[position]
        Log.d("PlaylistActivity", album.musicList.size.toString())
        Picasso.get()
            .load(album.imageSource)
            .error(R.drawable._31f9ec4b_d0d1_4f4b_a104_94450e11e8da)
            .into(binding.imageView1)
        lifecycleScope.launch {
            viewModel.viewModelShowMusic(album)
            viewModel.ready.collect {
                if (it) {
                    binding.progressBar2.visibility = View.INVISIBLE
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
        binding.backbtn.setOnClickListener {
            onBackPressed()
        }
    }
    private fun setRecyclerView(){
        binding.recyclerView.run {
            adapter = viewModel.adapter
            setHasFixedSize(true)
        }
    }
}

