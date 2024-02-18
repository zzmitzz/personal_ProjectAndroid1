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
        for(music in ext.realm.query(Music::class).find().toList()){
            if(music.title.lowercase().contains(newText!!.lowercase())){
                filterList.add(music)
            }
            musicAdapter.setMusic(filterList)
        }
    }
}