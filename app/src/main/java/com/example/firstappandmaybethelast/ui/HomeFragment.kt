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
import com.example.firstappandmaybethelast.databinding.FragmentHomeBinding
import com.example.firstappandmaybethelast.ext
import com.example.firstappandmaybethelast.service.MusicPlayerActivity
import com.example.firstappandmaybethelast.uiactivity.ArtistActivityLazy
import com.example.firstappandmaybethelast.uiactivity.PlaylistActivityLazy
import com.example.firstappandmaybethelast.viewmodel.HomeViewModel


class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<HomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {


        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.open()
        }
        viewModel.musicAdapter.onItemClick = {
            ext.customListMusic = viewModel.listTop10song
            Intent(context,MusicPlayerActivity::class.java).apply {
                putExtra("mediaPosition", it)
                startActivity(this)
            }
        }
        viewModel.musicPlaylistAdapter.itemClick = {

            Intent(context,PlaylistActivityLazy::class.java).apply {
                putExtra("album", it)
                startActivity(this)
            }
        }
        viewModel.artistListAdapter.onItemClick = {
            Intent(context,ArtistActivityLazy::class.java).apply {
                putExtra("artist", it)
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
        setupRecycleView()

        return binding.root
    }

    private fun setupRecycleView(){
        binding.rcv1.run {
            setHasFixedSize(true)
            adapter = viewModel.musicPlaylistAdapter
        }
        binding.rcv2.run{
            setHasFixedSize(true)
            adapter = viewModel.artistListAdapter
        }
        binding.rcv3.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter= viewModel.musicAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }
}