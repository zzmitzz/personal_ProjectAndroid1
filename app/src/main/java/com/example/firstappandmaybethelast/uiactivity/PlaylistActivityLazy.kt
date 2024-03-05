package com.example.firstappandmaybethelast.uiactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firstappandmaybethelast.databinding.FragmentPlaylistBinding

// Not view pager yet, Update it soon. Now let fuck up with activity


class PlaylistActivityLazy: AppCompatActivity() {
    private val binding by lazy {
        FragmentPlaylistBinding.inflate(layoutInflater)
    }
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        position= intent.getIntExtra("album",0)
        setRecyclerView()
    }
    private fun setRecyclerView(){
        binding.recyclerView.run {
//            adapter = MusicListAdapter().apply { setList(ext.customAlbumList[position]) }
            setHasFixedSize(true)
        }
    }
}

