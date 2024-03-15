package com.example.firstappandmaybethelast.viewmodel

import androidx.lifecycle.ViewModel
import com.example.firstappandmaybethelast.adapter.ArtistListAdapter
import com.example.firstappandmaybethelast.adapter.MusicAdapter
import com.example.firstappandmaybethelast.adapter.MusicListAdapter
import com.example.firstappandmaybethelast.ext

class HomeViewModel: ViewModel() {
    private val _musicAdapter = MusicAdapter()
    private val _musicPlaylistAdapter = MusicListAdapter()
    private val _artistListAdapter = ArtistListAdapter()
    val listTop10song = ext.getMusicData().shuffled().take(10)
    val musicPlaylistAdapter : MusicListAdapter
        get() = _musicPlaylistAdapter
    val musicAdapter: MusicAdapter
        get() = _musicAdapter
    val artistListAdapter: ArtistListAdapter
        get() = _artistListAdapter
    init {
        ext.customAlbumList = ext.getAlbumData().toMutableList()
        _artistListAdapter.setData(ext.getArtistData())
        _musicPlaylistAdapter.setList(ext.customAlbumList)
        _musicAdapter.setMusic(listTop10song)
    }
}