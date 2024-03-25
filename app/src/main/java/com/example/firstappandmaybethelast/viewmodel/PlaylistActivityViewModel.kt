package com.example.firstappandmaybethelast.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.firstappandmaybethelast.adapter.MusicAdapter
import com.example.firstappandmaybethelast.ext
import com.example.firstappandmaybethelast.realmdb.Album
import com.example.firstappandmaybethelast.realmdb.Music
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistActivityViewModel : ViewModel() {
    private var _adapter = MusicAdapter()
    val adapter: MusicAdapter
        get() = _adapter
    var ready = MutableStateFlow(false)
    private var listMusic: MutableList<Music> = mutableListOf()
    fun viewModelShowMusic(album: Album) {
        CoroutineScope(Dispatchers.IO).launch{
            for(id in album.musicList){
                Log.d("ArtistTest", id)
                var music: com.example.firstappandmaybethelast.musicdata.Music? = ext.getSongByID(id)
                if(music != null){
                    listMusic.add(ext.cvtToRealmMusic(music))
                }
            }
            withContext(Dispatchers.Main){_adapter.setMusic(listMusic)}
            ext.customListMusic = listMusic
            ready.update {true}
            }
        }

}
