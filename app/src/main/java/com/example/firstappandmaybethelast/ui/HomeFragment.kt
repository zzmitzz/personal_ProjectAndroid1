package com.example.firstappandmaybethelast.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstappandmaybethelast.adapter.MusicAdapter
import com.example.firstappandmaybethelast.adapter.MusicListAdapter
import com.example.firstappandmaybethelast.databinding.FragmentHomeBinding
import com.example.firstappandmaybethelast.musicdata.Music
import com.example.firstappandmaybethelast.musicdata.MusicData
import com.example.firstappandmaybethelast.service.MusicPlayerActivity


class HomeFragment : Fragment() {
    private lateinit var musicList: List<Music>
    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private val musicAdapter by lazy {
        MusicAdapter()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        musicList = MusicData.musicList
        musicAdapter.onItemClick = {
            Intent(context,MusicPlayerActivity::class.java).apply {
                putExtra("mediaPosition", it)
                startActivity(this)
            }
        }
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle ?
    ): View {
        musicAdapter.setMusic(musicList)
        setupRecycleView()
        return binding.root
    }

    private fun setupRecycleView(){
        binding.rcv1.run {
            setHasFixedSize(true)
            adapter = MusicListAdapter()
        }
        binding.rcv2.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter= musicAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }
}