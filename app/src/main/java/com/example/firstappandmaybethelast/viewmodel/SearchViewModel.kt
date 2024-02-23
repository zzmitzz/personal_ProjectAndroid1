package com.example.firstappandmaybethelast.viewmodel

import androidx.lifecycle.ViewModel
import com.example.firstappandmaybethelast.adapter.MusicAdapter
import com.example.firstappandmaybethelast.ext
import com.example.firstappandmaybethelast.realmdb.Music

class SearchViewModel: ViewModel() {
    private var _musicAdapter = MusicAdapter()
    val musicAdapter = _musicAdapter
    fun filterArray(newText: String?){
        val filterList = ArrayList<Music>()
        for(music in ext.listMusic){
            if(music.title.lowercase().contains(newText!!.lowercase())){
                filterList.add(music)
            }
        }
        ext.listMusic = filterList
        musicAdapter.setMusic(filterList)
    }
}