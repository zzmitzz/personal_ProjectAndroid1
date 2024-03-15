package com.example.firstappandmaybethelast.viewmodel

import androidx.lifecycle.ViewModel
import com.example.firstappandmaybethelast.adapter.ArtistListAdapter
import com.example.firstappandmaybethelast.adapter.MusicAdapter
import com.example.firstappandmaybethelast.ext
import com.example.firstappandmaybethelast.realmdb.Music
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel: ViewModel() {

    private val _artistAdapter = ArtistListAdapter()
    public val artistListAdapter: ArtistListAdapter
            get() = _artistAdapter

    private val _musicAdapter = MusicAdapter()
    val musicAdapter: MusicAdapter
        get() = _musicAdapter

    init {
        prepareData()
    }
    private fun prepareData(){
        if(ext.isLogin){
            CoroutineScope(Dispatchers.IO).launch {
                val listMusic : MutableList<Music?> = mutableListOf()
                for( id in ext.user?.favoriteMusic!!){
                    var tmp = ext.getSongByID(id)
                    if(tmp != null){
                        listMusic.add(ext.cvtToRealmMusic(tmp))
                    }
                }
                delay(2000)
                withContext(Dispatchers.Main){
                    _musicAdapter.setMusic(listMusic.toList())
                }
            }
        }
    }

}