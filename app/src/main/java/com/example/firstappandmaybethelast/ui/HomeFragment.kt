package com.example.firstappandmaybethelast.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstappandmaybethelast.adapter.MusicListAdapter
import com.example.firstappandmaybethelast.databinding.FragmentHomeBinding
import com.example.firstappandmaybethelast.musicdata.MusicData.musicList
import com.example.firstappandmaybethelast.service.MusicPlayerActivity
import com.example.firstappandmaybethelast.viewmodel.HomeViewModel


class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<HomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.musicAdapter.onItemClick = {
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
        viewModel.musicAdapter.setMusic(musicList)
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
            adapter= viewModel.musicAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }
}