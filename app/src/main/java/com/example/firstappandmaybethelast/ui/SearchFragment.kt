package com.example.firstappandmaybethelast.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstappandmaybethelast.adapter.MusicAdapter
import com.example.firstappandmaybethelast.databinding.FragmentSearchBinding
import com.example.firstappandmaybethelast.musicdata.Music
import com.example.firstappandmaybethelast.musicdata.MusicData
import com.example.firstappandmaybethelast.service.MusicPlayerActivity

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    private val binding by lazy {
        FragmentSearchBinding.inflate(layoutInflater)
    }
    private lateinit var musicList: List<Music>

    private val musicAdapter = MusicAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        musicList = MusicData.musicList
        musicAdapter.setMusic(musicList)
        binding.rcvSearch.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = musicAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        musicAdapter.onItemClick = {
            val intent = Intent(context, MusicPlayerActivity::class.java)
            intent.putExtra("mediaPosition", it)
            startActivity(intent)
        }
        activity?.runOnUiThread {
            binding.searchView.performClick()

        }
        binding.imageButton2.setOnClickListener { Toast.makeText(context,"is developing",Toast.LENGTH_SHORT).show() }

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filterArray(newText)
                return true
            }
        })
        super.onViewCreated(view, savedInstanceState)
    }

    fun filterArray(newText: String?){
        val filterList = ArrayList<Music>()
        for(music in musicList){
            if(music.title.lowercase().contains(newText!!.lowercase())){
                filterList.add(music)
            }
            musicAdapter.setMusic(filterList)
        }
    }

    companion object{
        const val TAG: String = "Search Fragment"
    }
}